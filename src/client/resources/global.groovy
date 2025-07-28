import ru.cws.betterchat.BetterChatMod
import ru.cws.betterchat.util.GroovyAdapter
import java.util.function.Function
import java.util.regex.Pattern

static void main(GroovyAdapter adapter) {
    var parser = getCWSParser()
    configCommonCategory(adapter, parser)
    createSimpleCategory(adapter, parser, "group", "Группа", "Чат группы", null, "@", Pattern.compile("^(§.)*\\[(§.)*party(§.)*]"))
    createSimpleCategory(adapter, parser, "guild", "Гильдия", "Чат поселения", "tc", null, Pattern.compile("^(§.)*\\[(§.)*TC(§.)*]"))
    createSimpleCategory(adapter, parser, "nation", "Альянс", "Чат нации", "nc", null, Pattern.compile("^(§.)*\\[(§.)*NC(§.)*]"))
    // Самой лучшей подруге на свете посвящается <3
//    configBestCategory(adapter, parser) // Секретная категория
}

static Function<String, Tuple3<String, String, String>> getCWSParser() {
    return (String message) -> {
        var i = message.indexOf(':')
        var j = message.lastIndexOf(' ', i)
        if (i == -1 || j == -1)
            return new Tuple3<String, String, String>("", null, message)
        var prefix = message.substring(0, j)
        var sender = message.substring(j + 1, i)
        var content = message.substring(i + 1)
        return new Tuple3<String, String, String>(prefix, sender, content)
    }
}

static void configCommonCategory(GroovyAdapter adapter, Function<String, Tuple3<String, String, String>> parser) {
    var category = adapter.getCategory("common")

    adapter.setCategoryOnOpen(category, { adapter.executeCommand(BetterChatMod.GLOBAL_LOCAL ? "g" : "lc") })
    adapter.setCategoryFormatToSend(category, (String message) -> message)

    var localPrefixPattern = Pattern.compile("^(§.)*\\[(§.)*local(§.)*]")
    var globalPrefixPattern = Pattern.compile("^(§.)*\\[(§.)*g(§.)*]")
    adapter.setCategoryTryAccept(category, (String message, String messageFmt, boolean self) -> {
        String content
        var localMatcher = localPrefixPattern.matcher(message)
        var globalMatcher = globalPrefixPattern.matcher(message)
        if (BetterChatMod.CATEGORY_FORMATTING) {
            if (localMatcher.find())
                content = localMatcher.replaceFirst(BetterChatMod.LOCAL_CHAT_PREFIX)
            else if (globalMatcher.find())
                content = globalMatcher.replaceFirst("")
            else return false
            var parsed = parser.apply(content)
            adapter.commonCategoryAccept(category, parsed.v1, parsed.v2, parsed.v3)
        } else {
            if (localMatcher.find() || globalMatcher.find())
                adapter.commonCategoryAcceptNoFmt(messageFmt)
            else return false
        }
        return true
    })
}

static createSimpleCategory(
        GroovyAdapter adapter,
        Function<String, Tuple3<String, String, String>> parser,
        String id,
        String name,
        String description,
        String command,
        String prefix,
        Pattern pattern
) {
    var category = adapter.getOrCreateCategory(id, name, description)

    adapter.setCategoryOnOpen(category, command == null ? { } : { adapter.executeCommand(command) })
    adapter.setCategoryFormatToSend(category, prefix == null ? (String message) -> message : (String message) -> prefix + " " + message)

    adapter.setCategoryTryAccept(category, (String message, String messageFmt, boolean self) -> {
        var matcher = pattern.matcher(messageFmt)
        if (matcher.find()) {
            if (BetterChatMod.CATEGORY_FORMATTING) {
                var content = parser.apply(messageFmt)
                adapter.categoryAccept(category, adapter.createMessage(content.v1, content.v2, content.v3))
                adapter.commonCategoryAccept(category, content.v1, content.v2, content.v3)
            } else {
                adapter.categoryAccept(category, adapter.createLiteral(messageFmt))
                adapter.commonCategoryAcceptNoFmt(messageFmt)
            }
            return true
        }
        return false
    });
}

static configBestCategory(GroovyAdapter adapter, Function<String, Tuple3<String, String, String>> parser) {
    var category = adapter.getOrCreateCategory("best", "О прекрасном", "Список всех сообщений с упоминанием Екатерины")

    adapter.setCategoryOnOpen(category, { })
    adapter.setCategoryFormatToSend(category, (String message) -> message)

    var filterPattern = Pattern.compile("((Ек|К)ат(е((чк(а|е|ой|у|и))|(ньк(а|е|ой|у|и))|(рин(а|е|ка|ой|у|ы)?)|й)?|и|ь|ю(ня|(х([аеиу])|(ш(а|ей?|у|и)?))?)?|я)|(([Мм])аков ([Цц])вет))")
    adapter.setCategoryTryAccept(category, (String message, String messageFmt, boolean self) -> {
        if (filterPattern.matcher(messageFmt.replaceAll("(§.)*", "")).find()) {
            if (BetterChatMod.CATEGORY_FORMATTING) {
                var content = parser.apply(message)
                adapter.categoryAccept(category, adapter.createMessage(content.v1, content.v2, content.v3))
            } else {
                adapter.categoryAccept(category, adapter.createLiteral(messageFmt))
            }
            return true
        }
        return false
    })
}