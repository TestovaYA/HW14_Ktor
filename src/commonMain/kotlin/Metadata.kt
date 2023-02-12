data class MetadataRequest(
    val group: String,
    val artifact: String,
    val version: String
)

data class MetadataResponse(
    val name: String,
    val url: String,
    val license: String
)
