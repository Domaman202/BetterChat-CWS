import ru.cws.betterchat.util.GroovyAdapter
import java.util.function.Function
import java.util.regex.Pattern

static void main(GroovyAdapter adapter) {
    var parser = getVanillaParser()
    configCommonCategory(adapter, parser)
    configPrefixCategory(adapter, parser, "trade", "Торговый", "Игровой чат для покупки и продажи ресурсов", "[:trade:]", true, true)
    configPrefixCategory(adapter, parser, "support", "Поддержка", "Игровой чат для технической поддержки", "[:support:]", true, false)
    // Самой лучшей подруге на свете посвящается <3
    configBestCategory(adapter, parser)
}

static Function<String, Tuple3<String, String, String>> getVanillaParser() {
    var vanillaSenderPattern = Pattern.compile("<[a-zA-Z0-9_]{3,16}> ")
    return (String message) -> {
        var matcher = vanillaSenderPattern.matcher(message)
        if (matcher.find()) {
            var sender = matcher.group(0)
            return new Tuple3("", sender.substring(1, sender.length() - 2), matcher.replaceAll(""))
        }
        return new Tuple3("", null, message)
    }
}

static void configCommonCategory(GroovyAdapter adapter, Function<String, Tuple3<String, String, String>> parser) {
    var category = adapter.getCategory("common")

    adapter.setCategoryOnOpen(category, { })
    adapter.setCategoryFormatToSend(category, (String message) -> "[:common:]" + message)

    var prefixPattern = Pattern.compile(Pattern.quote("[:common:]"))
    adapter.setCategoryTryAccept(category, (String message, boolean self) -> {
        var content = parser.apply(message)
        if (category != adapter.getSelectedCategory() || !(self || content.v1 == adapter.getPlayerName())) {
            var matcher = prefixPattern.matcher(content.v3)
            if (matcher.find())
                content = new Tuple3<String, String, String>(content.v1, content.v2, matcher.replaceAll(""))
            else return false
        }
        adapter.commonCategoryAccept(category, content.v1, content.v2, content.v3)
        return true
    })
}

static configBestCategory(GroovyAdapter adapter, Function<String, Tuple3<String, String, String>> parser) {
    var category = adapter.getOrCreateCategory("best", "О прекрасном", "Список всех сообщений с упоминанием Екатерины")

    adapter.setCategoryOnOpen(category, { })
    adapter.setCategoryFormatToSend(category, (String message) -> message)

    var filterPattern = Pattern.compile("((Ек|К)ат(е((чк(а|е|ой|у|и))|(ньк(а|е|ой|у|и))|(рин(а|е|ка|ой|у|ы)?)|й)?|и|ь|ю(ня|(х([аеиу])|(ш(а|ей?|у|и)?))?)?|я)|(([Мм])аков ([Цц])вет))")
    adapter.setCategoryTryAccept(category, (String message, boolean self) -> {
        var content = parser.apply(message)
        if (filterPattern.matcher(content.v3).find()) {
            adapter.categoryAccept(category, adapter.createMessage(content.v1, content.v2, content.v3))
            return true
        }
        return false
    })
}

static void configPrefixCategory(
        GroovyAdapter adapter,
        Function<String, Tuple3<String, String, String>> parser,
        String id,
        String name,
        String description,
        String prefix,
        boolean replacePrefix,
        boolean sendToCommon
) {
    var category = adapter.getOrCreateCategory(id, name, description)

    adapter.setCategoryOnOpen(category, { })
    adapter.setCategoryFormatToSend(category, (String message) -> prefix + message)

    var prefixPattern = Pattern.compile(Pattern.quote(prefix))
    adapter.setCategoryTryAccept(category, (String message, boolean self) -> {
        var content = parser.apply(message)
        var accepting = null

        if (category == adapter.getSelectedCategory() && (self || content.v1 == adapter.getPlayerName())) {
            accepting = adapter.createMessage(content.v1, content.v2, content.v3)
        } else {
            var matcher = prefixPattern.matcher(content.v3)
            if (matcher.find()) {
                content = new Tuple3<String, String, String>(content.v1, content.v2, replacePrefix ? matcher.replaceAll("") : content.v3)
                accepting = adapter.createMessage(content.v1, content.v2, content.v3)
            }
        }

        if (accepting == null)
            return false
        adapter.categoryAccept(category, accepting)
        if (sendToCommon)
            adapter.commonCategoryAccept(category, content.v1, content.v2, content.v3)
        return true
    })
}