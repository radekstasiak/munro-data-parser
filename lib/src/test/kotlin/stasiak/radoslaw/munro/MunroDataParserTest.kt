package stasiak.radoslaw.munro

import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.nio.file.Paths
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MunroDataParserTest {

    lateinit var parser: MunroDataParser
    lateinit var testDataFileInputStream: FileInputStream

    @Before
    fun setup() {
        MockKAnnotations.init(this)

//        testDataInputStream = MunroDataParserTest::class.java.getResourceAsStream("/munrotab_v6.2.csv")
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_v6.2.csv")
        testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
    }

    @Test
    fun `init method parses adds all line to the list`() {
        val parser = MunroDataParser(testDataFileInputStream)

        assertEquals(611, parser.getResults().size)
    }
}