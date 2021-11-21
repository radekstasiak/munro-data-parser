package stasiak.radoslaw.munro

import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream
import java.net.URL
import java.nio.file.Paths
import kotlin.test.assertEquals


class MunroDataParserTest {

    lateinit var parser: MunroDataParser
    lateinit var testDataFileInputStream: FileInputStream

    @Before
    fun setup() {
        MockKAnnotations.init(this)

//        testDataInputStream = MunroDataParserTest::class.java.getResourceAsStream("/munrotab_v6.2.csv")
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_v6.2.csv")!!
        testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
    }

    @Test
    fun `init method parses adds all line to the list`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")

        assertEquals(610, parser.getResults().size)
    }

    @Test
    fun `parser reads headers correctly`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")
        assertEquals(29, parser.getHeaders().size)
    }

    @Test
    fun `parser stores correct position of required columns`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")
        assertEquals(4, parser.getHeaderListMap().size)
        assertEquals(5, parser.getHeaderListMap()["Name"])
        assertEquals(9, parser.getHeaderListMap()["Height (m)"])
        assertEquals(27, parser.getHeaderListMap()["Post 1997"])
        assertEquals(13, parser.getHeaderListMap()["Grid Ref"])
    }

    @Test
    fun `parser stores correct position of required columns when headers are reordered`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_reordered_headers.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        assertEquals(4, parser.getHeaderListMap().size)
        assertEquals(12, parser.getHeaderListMap()["Name"])
        assertEquals(21, parser.getHeaderListMap()["Height (m)"])
        assertEquals(1, parser.getHeaderListMap()["Post 1997"])
        assertEquals(7, parser.getHeaderListMap()["Grid Ref"])
    }

    @Test
    fun `parser ignores empty line`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_empty_line.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        assertEquals(2, parser.getResults().size)
    }

    @Test
    fun `parsers map MunroDataRecords correctly`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDatRecords = parser.getResults()
        assertEquals("Ben Chonzie", munroDatRecords[0].name)
        assertEquals("931", munroDatRecords[0].heightInMeters)
        assertEquals("MUN", munroDatRecords[0].hillCategory)
        assertEquals("NN773308", munroDatRecords[0].gridRef)

    }


    //test position with updated column order
    //return error when some of the columns are missing

    //test for empty lines
    //test for malformed file
    //test for any other file being uploaded

    //test when you'll be requiring for the element of the line array, for the required column position, and make sure it exists there
    //maybe also check wheter number opf the line size is equal to the total headers, this way you'll know if the given row comes along with the header structure


    //remember to update Readme with usage of the library and how to build it
}