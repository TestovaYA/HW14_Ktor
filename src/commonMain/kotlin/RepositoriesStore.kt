data class RepositoriesStore(
    val repositories: MutableList<RepositoryInfo>
)

data class RepositoryInfo(
    val id: Int,
    val name: String,
    val url: String
)

data class RepositoryId(
    val id: Int
)

data class RepositoryRequest(
    val name: String,
    val url: String
)