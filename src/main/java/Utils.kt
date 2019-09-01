fun calculateMemoryUsedInMegabytes() = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / DATASIZE_MB

const val DATASIZE_MB = 1024 * 1024