import flashtext.KeywordProcessor as KeywordProcessor

fun main(args: Array<String>) {
    val keywordProcessor = KeywordProcessor(caseSensitive=true)
    keywordProcessor.addKeyword("NYC", "New York")
    keywordProcessor.addKeyword("APPL", "Apple")
    keywordProcessor.addKeywordsFromMap(
        hashMapOf(
            ("java" to arrayListOf("java_2e", "java programing")),
            ("product manager" to arrayListOf("PM", "product manager"))
        )
    )
    println("Terms in Trie: ${keywordProcessor.size()}")
    println("Data: ${keywordProcessor.getAllKeywords().toString()}")

    val text: String = "I am a PM for a java_2e platform working from APPL, NYC"
    println("Text: ${text}")
    println("Extract: ${keywordProcessor.extractKeywords(text)}")
    println("Replace: ${keywordProcessor.replaceKeywords(text)}")
}
