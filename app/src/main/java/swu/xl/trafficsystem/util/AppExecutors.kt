package swu.xl.trafficsystem.util

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object AppExecutors {
    private val AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors()

    @Volatile
    private var _IO: Executor? = null
    private var _Default: Executor? = null
    private var _Single: Executor? = null

    @JvmStatic
    val IO: Executor
        get() {
            return _IO ?: Executors.newFixedThreadPool(
                64.coerceAtLeast(AVAILABLE_PROCESSORS),
                object : ThreadFactory {
                    private val count = AtomicInteger(1)
                    override fun newThread(r: Runnable): Thread {
                        return Thread(r, "Calidge.IO #${count.getAndIncrement()}")
                    }
                }).also {
                _IO = it
            }
        }

    @JvmStatic
    val Default: Executor
        get() {
            return _Default ?: kotlin.run {
                val nThreads = (AVAILABLE_PROCESSORS - 1).coerceAtLeast(1)
                ThreadPoolExecutor(
                    nThreads, nThreads,
                    30L, TimeUnit.SECONDS,
                    LinkedBlockingQueue<Runnable>(),
                    object : ThreadFactory {
                        private val count = AtomicInteger(1)
                        override fun newThread(r: Runnable): Thread {
                            return Thread(r, "Calidge.Default #${count.getAndIncrement()}")
                        }
                    }).also {
                    _Default = it
                }
            }
        }

    @JvmStatic
    val Single: Executor
        get() {
            return _Single?: kotlin.run {
                ThreadPoolExecutor(
                    1, 1,
                    30L, TimeUnit.SECONDS,
                    LinkedBlockingQueue<Runnable>(),
                    object : ThreadFactory {
                        private val count = AtomicInteger(1)
                        override fun newThread(r: Runnable): Thread {
                            return Thread(r, "Calidge.Single")
                        }
                    }).also {
                    _Single = it
                }
            }
        }
}