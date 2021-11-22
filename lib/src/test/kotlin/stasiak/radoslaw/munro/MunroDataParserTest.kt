package stasiak.radoslaw.munro

import org.junit.Assert.assertThrows
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
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_v6.2.csv")!!
        testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
    }

    @Test
    fun `init method parses adds all line to the list`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")

        assertEquals(602, parser.getResults().size)
    }

    @Test
    fun `parser reads headers correctly`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")
        assertEquals(29, parser.getHeaders().size)
    }

//    @Test
//    fun `parser stores correct position of required columns`() {
//        val parser = MunroDataParser(testDataFileInputStream, ",")
//        assertEquals(4, parser.getHeaderListMap().size)
//        assertEquals(5, parser.getHeaderListMap()["Name"])
//        assertEquals(9, parser.getHeaderListMap()["Height (m)"])
//        assertEquals(27, parser.getHeaderListMap()["Post 1997"])
//        assertEquals(13, parser.getHeaderListMap()["Grid Ref"])
//    }
//
//    @Test
//    fun `parser stores correct position of required columns when headers are reordered`() {
//        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_reordered_headers.csv")!!
//        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
//        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
//        parser.getResults()
//        assertEquals(4, parser.getHeaderListMap().size)
//        assertEquals(12, parser.getHeaderListMap()["Name"])
//        assertEquals(21, parser.getHeaderListMap()["Height (m)"])
//        assertEquals(1, parser.getHeaderListMap()["Post 1997"])
//        assertEquals(7, parser.getHeaderListMap()["Grid Ref"])
//    }

    @Test
    fun `parser ignores empty line`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_empty_line.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        assertEquals(2, parser.getResults().size)
        assertEquals(29, parser.getHeaders().size)
    }

    @Test
    fun `parser ignores non-munro recrods and maps active records correctly`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDatRecords = parser.getResults()

        assertEquals(10, munroDatRecords.size)

        //name=Ben Chonzie, heightInMeters=931, hillCategory=MUN, gridRef=NN773308
        assertEquals("Ben Chonzie", munroDatRecords[0].fieldsMap["Name"])
        assertEquals("931", munroDatRecords[0].fieldsMap["Height (m)"])
        assertEquals("MUN", munroDatRecords[0].fieldsMap["Post 1997"])
        assertEquals("NN773308", munroDatRecords[0].fieldsMap["Grid Ref"])

        //name=, heightInMeters=1216, hillCategory=TOP, gridRef=NH985025
        assertEquals("", munroDatRecords[2].fieldsMap["Name"])
        assertEquals("1216", munroDatRecords[2].fieldsMap["Height (m)"])
        assertEquals("TOP", munroDatRecords[2].fieldsMap["Post 1997"])
        assertEquals("NH985025", munroDatRecords[2].fieldsMap["Grid Ref"])

        //name=Sgurr na Banachdich, heightInMeters=, hillCategory=MUN, gridRef=NG440224
        assertEquals("Sgurr na Banachdich", munroDatRecords[5].fieldsMap["Name"])
        assertEquals("", munroDatRecords[5].fieldsMap["Height (m)"])
        assertEquals("MUN", munroDatRecords[5].fieldsMap["Post 1997"])
        assertEquals("NG440224", munroDatRecords[5].fieldsMap["Grid Ref"])

        //name=Sgurr na Banachdich - Sgurr Thormaid, heightInMeters=926, hillCategory=, gridRef=NG441226
        assertEquals("Sgurr na Banachdich - Sgurr Thormaid", munroDatRecords[7].fieldsMap["Name"])
        assertEquals("926", munroDatRecords[7].fieldsMap["Height (m)"])
        assertEquals("", munroDatRecords[7].fieldsMap["Post 1997"])
        assertEquals("NG441226", munroDatRecords[7].fieldsMap["Grid Ref"])

        //name=Ben More, heightInMeters=966, hillCategory=MUN, gridRef=
        assertEquals("Ben More", munroDatRecords[9].fieldsMap["Name"])
        assertEquals("966", munroDatRecords[9].fieldsMap["Height (m)"])
        assertEquals("MUN", munroDatRecords[9].fieldsMap["Post 1997"])
        assertEquals("", munroDatRecords[9].fieldsMap["Grid Ref"])
    }

    @Test
    fun `parsers map MunroDataRecords correctly when required columns are missing`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_required_columns_missing.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDatRecords = parser.getResults()

        assertEquals(1, munroDatRecords.size)

//        assertEquals("", munroDatRecords[0].name)
//        assertEquals("", munroDatRecords[0].heightInMeters)
//        assertEquals("", munroDatRecords[0].hillCategory)
//        assertEquals("", munroDatRecords[0].gridRef)
    }
//
//    @Test
//    fun `parsers throws exception with correct message when Name header is missing`() {
//        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_header_name_missing.csv")!!
//        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
//
//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            MunroDataParser(reorderedHeadersDataFileInputStream, ",")
//        }
//        assertEquals("Required headers are missing:`Name`", exception.message)
//    }
//
//    @Test
//    fun `parsers throws exception with correct message when Grid Ref header is missing`() {
//        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_header_gridref_missing.csv")!!
//        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
//
//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            MunroDataParser(reorderedHeadersDataFileInputStream, ",")
//        }
//        assertEquals("Required headers are missing:`Grid Ref`", exception.message)
//    }
//
//    @Test
//    fun `parsers throws exception with correct message when all of the required headers are missing`() {
//        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_all_required_headers_missing.csv")!!
//        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
//
//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            MunroDataParser(reorderedHeadersDataFileInputStream, ",")
//        }
//        assertEquals("Required headers are missing:`Name`,`Height (m)`,`Post 1997`,`Grid Ref`", exception.message)
//    }


}