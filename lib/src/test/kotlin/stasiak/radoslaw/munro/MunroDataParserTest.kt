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

    @Test
    fun `test`() {
        //test lat field empty
        //test first field NOT empty
        //test first field has quotes
        //test 3 quotes in side quote field
        val csvRecord =
            ",1,\"http://www.streetmap.co.uk/newmap.srf?x=277324&y=730857&z=3&sv=277324,730857&st=4&tl=~&bi=~&lu=N&ar=y\",http://www.geograph.org.uk/gridref/NN7732430857,http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=1,Ben Chonzie,1,01A,1.1,931,3054,51 52,OL47W 368W 379W,NN773308,NN7732430857,277324,730857,MUN,,TOP,\"Hej, to jest \"\" test\""
//            "\"Hej, to jest \"\" test\","
        val lexer = Lexer(csvRecord = csvRecord,",".single())
        val result = lexer.result

//        assertEquals("Hej, to jest \"\" test",result[0])

        assertEquals("",result[0])
        assertEquals("1",result[1])
        assertEquals("http://www.streetmap.co.uk/newmap.srf?x=277324&y=730857&z=3&sv=277324,730857&st=4&tl=~&bi=~&lu=N&ar=y",result[2])
        assertEquals("http://www.geograph.org.uk/gridref/NN7732430857",result[3])
        assertEquals("http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=1",result[4])
        assertEquals("Ben Chonzie",result[5])
        assertEquals("1",result[6])
        assertEquals("01A",result[7])
        assertEquals("1.1",result[8])
        assertEquals("931",result[9])
        assertEquals("3054",result[10])
        assertEquals("51 52",result[11])
        assertEquals("OL47W 368W 379W",result[12])
        assertEquals("NN773308",result[13])
        assertEquals("NN7732430857",result[14])
        assertEquals("277324",result[15])
        assertEquals("730857",result[16])
        assertEquals("MUN",result[17])
        assertEquals("",result[18])
        assertEquals("TOP",result[19])
        assertEquals("Hej, to jest \"\" test",result[20])

        assertEquals(21, result.size)

    }

    //test position with updated column order
    //return error when some of the columns are missing

    //test for empty lines
    //test for malformed file
    //test for any other file being uploaded
    //test for case when comment or some other column contains a delimeter, see how the data is being exported in such case
    //test for data with commas etc.

    //test when you'll be requiring for the element of the line array, for the required column position, and make sure it exists there
    //maybe also check wheter number opf the line size is equal to the total headers, this way you'll know if the given row comes along with the header structure


    //remember to update Readme with usage of the library and how to build it
}