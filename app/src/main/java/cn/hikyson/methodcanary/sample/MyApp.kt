package cn.hikyson.methodcanary.sample

import android.app.Application
import cn.hikyson.methodcanary.lib.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("MethodCanary")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy));

        Thread(Runnable {
            for (i in 0..1000) {
                Thread.sleep(293)
                MethodCanaryInject.install(
                    MethodCanaryConfig.MethodCanaryConfigBuilder.aMethodCanaryConfig().app(this).methodEventThreshold(
                        5
                    ).methodCanaryOutputCallback { startTimeNanos, stopTimeNanos, methodEventsFile ->
                        Logger.d(
                            "startTimeNanos:%s, stopTimeNanos:%s, methodEventsFile:\n%s",
                            startTimeNanos,
                            stopTimeNanos,
                            readFile2BytesByChannel(methodEventsFile)?.let {
                                String(
                                    it,
                                    Charset.forName("utf-8")
                                )
                            })
                    }.build()
                )
            }
        }).start()

        Thread(Runnable {
            for (i in 0..1000) {
                Thread.sleep(189)
                MethodCanaryInject.uninstall()
            }
        }).start()

        Thread(Runnable {
            for (i in 0..10000) {
                Thread.sleep(42)
                try {
                    MethodCanaryInject.startMonitor()
                } catch (e: Throwable) {
                }
            }
        }).start()
        Thread(Runnable {
            for (i in 0..10000) {
                Thread.sleep(23)
                MethodCanaryInject.stopMonitor()
            }
        }).start()
    }

    internal fun readFile2BytesByChannel(file: File): ByteArray? {
        if (!isFileExists(file)) {
            return null
        }
        var fc: FileChannel? = null
        try {
            fc = RandomAccessFile(file, "r").channel
            val byteBuffer = ByteBuffer.allocate(fc!!.size().toInt())
            while (true) {
                if (fc.read(byteBuffer) <= 0) {
                    break
                }
            }
            return byteBuffer.array()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }
}