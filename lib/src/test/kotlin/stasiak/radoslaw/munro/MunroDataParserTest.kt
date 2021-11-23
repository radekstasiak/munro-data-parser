package stasiak.radoslaw.munro

import org.junit.Before
import org.junit.Test
import java.io.FileInputStream
import java.net.URL
import java.nio.file.Paths
import kotlin.test.assertEquals


class MunroDataParserTest {

    lateinit var testDataFileInputStream: FileInputStream

    @Before
    fun setup() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_v6.2.csv")!!
        testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
    }

    @Test
    fun `parser maps results correctly when no Query provided`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().build()
        assertEquals(509, parser.getResults(munroDataQuery).size)
    }

    @Test
    fun `parser reads headers correctly`() {
        val parser = MunroDataParser(testDataFileInputStream, ",")
        assertEquals(29, parser.getHeaders().size)
    }


    @Test
    fun `parser ignores empty line`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_empty_line.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().build()
        assertEquals(2, parser.getResults(munroDataQuery).size)
        assertEquals(29, parser.getHeaders().size)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(8, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals("931", munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("", munroDataResults[2].name)
        assertEquals("1216", munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NH985025", munroDataResults[2].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[4].name)
        assertEquals("", munroDataResults[4].heightInMeters)
        assertEquals("MUN", munroDataResults[4].hillCategory)
        assertEquals("NG440224", munroDataResults[4].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[6].name)
        assertEquals("926.5", munroDataResults[6].heightInMeters)
        assertEquals("TOP", munroDataResults[6].hillCategory)
        assertEquals("NG528215", munroDataResults[6].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when FilterByHilLCategory is TOP`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().filterByHillCategory(MunroDataQuery.MunroDataHillCategory.TOP).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("", munroDataResults[0].name)
        assertEquals("1216", munroDataResults[0].heightInMeters)
        assertEquals("TOP", munroDataResults[0].hillCategory)
        assertEquals("NH985025", munroDataResults[0].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[1].name)
        assertEquals("922", munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH232691", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[2].name)
        assertEquals("942", munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG441222", munroDataResults[2].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[3].name)
        assertEquals("926.5", munroDataResults[3].heightInMeters)
        assertEquals("TOP", munroDataResults[3].hillCategory)
        assertEquals("NG528215", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when FilterByHilLCategory is MUNRO`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().filterByHillCategory(MunroDataQuery.MunroDataHillCategory.MUNRO).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals("931", munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[1].name)
        assertEquals("985", munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN629189", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[2].name)
        assertEquals("", munroDataResults[2].heightInMeters)
        assertEquals("MUN", munroDataResults[2].hillCategory)
        assertEquals("NG440224", munroDataResults[2].gridRef)

        assertEquals("Ben More", munroDataResults[3].name)
        assertEquals("966", munroDataResults[3].heightInMeters)
        assertEquals("MUN", munroDataResults[3].hillCategory)
        assertEquals("", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when SetMinHeightInMeters is active`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().setMinHeightInMeters(942.0).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("Ben Vorlich", munroDataResults[0].name)
        assertEquals("985", munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN629189", munroDataResults[0].gridRef)

        assertEquals("", munroDataResults[1].name)
        assertEquals("1216", munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH985025", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[2].name)
        assertEquals("942", munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG441222", munroDataResults[2].gridRef)

        assertEquals("Ben More", munroDataResults[3].name)
        assertEquals("966", munroDataResults[3].heightInMeters)
        assertEquals("MUN", munroDataResults[3].hillCategory)
        assertEquals("", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when SetMaxHeightInMeters is active`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().setMaxHeightInMeters(942.0).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals("931", munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[1].name)
        assertEquals("922", munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH232691", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[2].name)
        assertEquals("942", munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG441222", munroDataResults[2].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[3].name)
        assertEquals("926.5", munroDataResults[3].heightInMeters)
        assertEquals("TOP", munroDataResults[3].hillCategory)
        assertEquals("NG528215", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when maxResults is set`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder().setResultsLimit(3).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(3, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals("931", munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[1].name)
        assertEquals("985", munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN629189", munroDataResults[1].gridRef)

        assertEquals("", munroDataResults[2].name)
        assertEquals("1216", munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NH985025", munroDataResults[2].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when multiple criterias are set`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
        val munroDataQuery = MunroDataQuery.Builder()
            .filterByHillCategory(MunroDataQuery.MunroDataHillCategory.MUNRO)
            .setMaxHeightInMeters(1100.0)
            .setMinHeightInMeters(926.5)
            .setResultsLimit(1).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(1, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals("931", munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

    }

//    @Test
//    fun `parsers map MunroDataRecords correctly when required columns are missing`() {
//        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_required_columns_missing.csv")!!
//        val reorderedHeadersDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
//        val parser = MunroDataParser(reorderedHeadersDataFileInputStream, ",")
//        val munroDataQuery = MunroDataQuery.Builder().build()
//        val munroDataResults = parser.getResults(munroDataQuery)
//
//        assertEquals(1, munroDataResults.size)

//        assertEquals("", munroDatRecords[0].name)
//        assertEquals("", munroDatRecords[0].heightInMeters)
//        assertEquals("", munroDatRecords[0].hillCategory)
//        assertEquals("", munroDatRecords[0].gridRef)
//    }
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