package org.studio.ghibli.background

import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.studio.ghibli.base.BaseTest

class LocationWorkerTest : BaseTest() {

    @Test
    fun testWorker() {
        val worker = TestListenableWorkerBuilder<LocationsWorker>(context).build()
        runBlocking {
            val result = worker.doWork()
            Assert.assertTrue(result == ListenableWorker.Result.success())
        }
    }
}
