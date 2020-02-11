package org.studio.ghibli.background

import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.studio.ghibli.base.BaseTest

class FilmsWorkerTest : BaseTest() {

    @Test
    fun testWorker() {
        val worker = TestListenableWorkerBuilder<FilmsWorker>(context).build()
        runBlocking {
            val result = worker.doWork()
            Assert.assertTrue(result == ListenableWorker.Result.success())
        }
    }
}
