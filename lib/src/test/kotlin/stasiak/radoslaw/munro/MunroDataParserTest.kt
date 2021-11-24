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
        val parser = MunroDataParser(testDataFileInputStream,)
        assertEquals(509, parser.getResults().size)
    }

    @Test
    fun `parser reads headers correctly`() {
        val parser = MunroDataParser(testDataFileInputStream)
        assertEquals(29, parser.getHeaders().size)
    }


    @Test
    fun `parser ignores empty line`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_empty_line.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        assertEquals(2, parser.getResults().size)
        assertEquals(29, parser.getHeaders().size)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataResults = parser.getResults()

        assertEquals(8, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals(931.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("", munroDataResults[2].name)
        assertEquals(1216.0, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NH985025", munroDataResults[2].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[4].name)
        assertEquals(0.0, munroDataResults[4].heightInMeters)
        assertEquals("MUN", munroDataResults[4].hillCategory)
        assertEquals("NG440224", munroDataResults[4].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[6].name)
        assertEquals(926.5, munroDataResults[6].heightInMeters)
        assertEquals("TOP", munroDataResults[6].hillCategory)
        assertEquals("NG528215", munroDataResults[6].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when FilterByHilLCategory is TOP`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery =
            MunroDataQuery.Builder().filterByHillCategory(MunroDataQuery.MunroDataHillCategory.TOP).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("", munroDataResults[0].name)
        assertEquals(1216.0, munroDataResults[0].heightInMeters)
        assertEquals("TOP", munroDataResults[0].hillCategory)
        assertEquals("NH985025", munroDataResults[0].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[1].name)
        assertEquals(922.0, munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH232691", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[2].name)
        assertEquals(942.0, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG441222", munroDataResults[2].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[3].name)
        assertEquals(926.5, munroDataResults[3].heightInMeters)
        assertEquals("TOP", munroDataResults[3].hillCategory)
        assertEquals("NG528215", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when FilterByHilLCategory is MUNRO`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery =
            MunroDataQuery.Builder().filterByHillCategory(MunroDataQuery.MunroDataHillCategory.MUNRO).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals(931.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[1].name)
        assertEquals(985.0, munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN629189", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[2].name)
        assertEquals(0.0, munroDataResults[2].heightInMeters)
        assertEquals("MUN", munroDataResults[2].hillCategory)
        assertEquals("NG440224", munroDataResults[2].gridRef)

        assertEquals("Ben More", munroDataResults[3].name)
        assertEquals(966.0, munroDataResults[3].heightInMeters)
        assertEquals("MUN", munroDataResults[3].hillCategory)
        assertEquals("", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when SetMinHeightInMeters is active`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder().setMinHeightInMeters(942.0).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("Ben Vorlich", munroDataResults[0].name)
        assertEquals(985.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN629189", munroDataResults[0].gridRef)

        assertEquals("", munroDataResults[1].name)
        assertEquals(1216.0, munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH985025", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[2].name)
        assertEquals(942.0, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG441222", munroDataResults[2].gridRef)

        assertEquals("Ben More", munroDataResults[3].name)
        assertEquals(966.0, munroDataResults[3].heightInMeters)
        assertEquals("MUN", munroDataResults[3].hillCategory)
        assertEquals("", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when SetMaxHeightInMeters is active`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder().setMaxHeightInMeters(942.0).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(4, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals(931.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[1].name)
        assertEquals(922.0, munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH232691", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[2].name)
        assertEquals(942.0, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG441222", munroDataResults[2].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[3].name)
        assertEquals(926.5, munroDataResults[3].heightInMeters)
        assertEquals("TOP", munroDataResults[3].hillCategory)
        assertEquals("NG528215", munroDataResults[3].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when maxResults is set`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder().setResultsLimit(3).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(3, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals(931.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[1].name)
        assertEquals(985.0, munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN629189", munroDataResults[1].gridRef)

        assertEquals("", munroDataResults[2].name)
        assertEquals(1216.0, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NH985025", munroDataResults[2].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when multiple filters are set`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder()
            .filterByHillCategory(MunroDataQuery.MunroDataHillCategory.MUNRO)
            .setMaxHeightInMeters(1100.0)
            .setMinHeightInMeters(926.5)
            .setResultsLimit(1).build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(1, munroDataResults.size)

        assertEquals("Ben Chonzie", munroDataResults[0].name)
        assertEquals(931.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NN773308", munroDataResults[0].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel and returns result sorted alphabetically in ascending order`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder()
            .setSortingRule(sortingRule = MunroDataQuerySortingRules.SortAlphabeticallyByName(true))
            .build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(8, munroDataResults.size)

        assertEquals("", munroDataResults[0].name)
        assertEquals(1216.0, munroDataResults[0].heightInMeters)
        assertEquals("TOP", munroDataResults[0].hillCategory)
        assertEquals("NH985025", munroDataResults[0].gridRef)

        assertEquals("Ben Chonzie", munroDataResults[1].name)
        assertEquals(931.0, munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN773308", munroDataResults[1].gridRef)

        assertEquals("Ben More", munroDataResults[2].name)
        assertEquals(966.0, munroDataResults[2].heightInMeters)
        assertEquals("MUN", munroDataResults[2].hillCategory)
        assertEquals("", munroDataResults[2].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[3].name)
        assertEquals(985.0, munroDataResults[3].heightInMeters)
        assertEquals("MUN", munroDataResults[3].hillCategory)
        assertEquals("NN629189", munroDataResults[3].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[4].name)
        assertEquals(926.5, munroDataResults[4].heightInMeters)
        assertEquals("TOP", munroDataResults[4].hillCategory)
        assertEquals("NG528215", munroDataResults[4].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[5].name)
        assertEquals(922.0, munroDataResults[5].heightInMeters)
        assertEquals("TOP", munroDataResults[5].hillCategory)
        assertEquals("NH232691", munroDataResults[5].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[6].name)
        assertEquals(0.0, munroDataResults[6].heightInMeters)
        assertEquals("MUN", munroDataResults[6].hillCategory)
        assertEquals("NG440224", munroDataResults[6].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[7].name)
        assertEquals(942.0, munroDataResults[7].heightInMeters)
        assertEquals("TOP", munroDataResults[7].hillCategory)
        assertEquals("NG441222", munroDataResults[7].gridRef)
    }


    @Test
    fun `parser maps MunroDataRecord to MunroDataModel and returns result sorted alphabetically in descending order`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder()
            .setSortingRule(MunroDataQuerySortingRules.SortAlphabeticallyByName())
            .build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(8, munroDataResults.size)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[0].name)
        assertEquals(942.0, munroDataResults[0].heightInMeters)
        assertEquals("TOP", munroDataResults[0].hillCategory)
        assertEquals("NG441222", munroDataResults[0].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[1].name)
        assertEquals(0.0, munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NG440224", munroDataResults[1].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[2].name)
        assertEquals(922.0, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NH232691", munroDataResults[2].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[3].name)
        assertEquals(926.5, munroDataResults[3].heightInMeters)
        assertEquals("TOP", munroDataResults[3].hillCategory)
        assertEquals("NG528215", munroDataResults[3].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[4].name)
        assertEquals(985.0, munroDataResults[4].heightInMeters)
        assertEquals("MUN", munroDataResults[4].hillCategory)
        assertEquals("NN629189", munroDataResults[4].gridRef)

        assertEquals("Ben More", munroDataResults[5].name)
        assertEquals(966.0, munroDataResults[5].heightInMeters)
        assertEquals("MUN", munroDataResults[5].hillCategory)
        assertEquals("", munroDataResults[5].gridRef)

        assertEquals("Ben Chonzie", munroDataResults[6].name)
        assertEquals(931.0, munroDataResults[6].heightInMeters)
        assertEquals("MUN", munroDataResults[6].hillCategory)
        assertEquals("NN773308", munroDataResults[6].gridRef)

        assertEquals("", munroDataResults[7].name)
        assertEquals(1216.0, munroDataResults[7].heightInMeters)
        assertEquals("TOP", munroDataResults[7].hillCategory)
        assertEquals("NH985025", munroDataResults[7].gridRef)

    }


    @Test
    fun `parser maps MunroDataRecord to MunroDataModel and returns result sorted by height in ascending order`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder()
            .setSortingRule(MunroDataQuerySortingRules.SortByHeightInMeters(true))
            .build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(8, munroDataResults.size)

        assertEquals("Sgurr na Banachdich", munroDataResults[0].name)
        assertEquals(0.0, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NG440224", munroDataResults[0].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[1].name)
        assertEquals(922.0, munroDataResults[1].heightInMeters)
        assertEquals("TOP", munroDataResults[1].hillCategory)
        assertEquals("NH232691", munroDataResults[1].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[2].name)
        assertEquals(926.5, munroDataResults[2].heightInMeters)
        assertEquals("TOP", munroDataResults[2].hillCategory)
        assertEquals("NG528215", munroDataResults[2].gridRef)

        assertEquals("Ben Chonzie", munroDataResults[3].name)
        assertEquals(931.0, munroDataResults[3].heightInMeters)
        assertEquals("MUN", munroDataResults[3].hillCategory)
        assertEquals("NN773308", munroDataResults[3].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[4].name)
        assertEquals(942.0, munroDataResults[4].heightInMeters)
        assertEquals("TOP", munroDataResults[4].hillCategory)
        assertEquals("NG441222", munroDataResults[4].gridRef)

        assertEquals("Ben More", munroDataResults[5].name)
        assertEquals(966.0, munroDataResults[5].heightInMeters)
        assertEquals("MUN", munroDataResults[5].hillCategory)
        assertEquals("", munroDataResults[5].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[6].name)
        assertEquals(985.0, munroDataResults[6].heightInMeters)
        assertEquals("MUN", munroDataResults[6].hillCategory)
        assertEquals("NN629189", munroDataResults[6].gridRef)

        assertEquals("", munroDataResults[7].name)
        assertEquals(1216.0, munroDataResults[7].heightInMeters)
        assertEquals("TOP", munroDataResults[7].hillCategory)
        assertEquals("NH985025", munroDataResults[7].gridRef)

    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel and returns result sorted by height in descending order`() {
        val resource: URL = MunroDataParserTest::class.java.getResource("/munrotab_10_results.csv")!!
        val testDataFileInputStream = FileInputStream(Paths.get(resource.toURI()).toFile())
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder()
            .setSortingRule(MunroDataQuerySortingRules.SortByHeightInMeters(false))
            .build()
        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(8, munroDataResults.size)

        assertEquals("", munroDataResults[0].name)
        assertEquals(1216.0, munroDataResults[0].heightInMeters)
        assertEquals("TOP", munroDataResults[0].hillCategory)
        assertEquals("NH985025", munroDataResults[0].gridRef)

        assertEquals("Ben Vorlich", munroDataResults[1].name)
        assertEquals(985.0, munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN629189", munroDataResults[1].gridRef)

        assertEquals("Ben More", munroDataResults[2].name)
        assertEquals(966.0, munroDataResults[2].heightInMeters)
        assertEquals("MUN", munroDataResults[2].hillCategory)
        assertEquals("", munroDataResults[2].gridRef)

        assertEquals("Sgurr na Banachdich Central Top", munroDataResults[3].name)
        assertEquals(942.0, munroDataResults[3].heightInMeters)
        assertEquals("TOP", munroDataResults[3].hillCategory)
        assertEquals("NG441222", munroDataResults[3].gridRef)

        assertEquals("Ben Chonzie", munroDataResults[4].name)
        assertEquals(931.0, munroDataResults[4].heightInMeters)
        assertEquals("MUN", munroDataResults[4].hillCategory)
        assertEquals("NN773308", munroDataResults[4].gridRef)

        assertEquals("Bla Bheinn SW Top", munroDataResults[5].name)
        assertEquals(926.5, munroDataResults[5].heightInMeters)
        assertEquals("TOP", munroDataResults[5].hillCategory)
        assertEquals("NG528215", munroDataResults[5].gridRef)

        assertEquals("Meall Gorm SE Top", munroDataResults[6].name)
        assertEquals(922.0, munroDataResults[6].heightInMeters)
        assertEquals("TOP", munroDataResults[6].hillCategory)
        assertEquals("NH232691", munroDataResults[6].gridRef)

        assertEquals("Sgurr na Banachdich", munroDataResults[7].name)
        assertEquals(0.0, munroDataResults[7].heightInMeters)
        assertEquals("MUN", munroDataResults[7].hillCategory)
        assertEquals("NG440224", munroDataResults[7].gridRef)
    }

    @Test
    fun `parser maps MunroDataRecord to MunroDataModel correctly when multiple filters and sorting rule are set`() {
        val parser = MunroDataParser(testDataFileInputStream)
        val munroDataQuery = MunroDataQuery.Builder()
            .filterByHillCategory(MunroDataQuery.MunroDataHillCategory.MUNRO)
            .setMinHeightInMeters(945.7)
            .setMaxHeightInMeters(948.0)
            .setResultsLimit(3)
            .setSortingRule(MunroDataQuerySortingRules.SortByHeightInMeters(ascending = true))
            .build()

        val munroDataResults = parser.getResults(munroDataQuery)

        assertEquals(3, munroDataResults.size)

        assertEquals("Carn Dearg", munroDataResults[0].name)
        assertEquals(945.7, munroDataResults[0].heightInMeters)
        assertEquals("MUN", munroDataResults[0].hillCategory)
        assertEquals("NH635023", munroDataResults[0].gridRef)

        assertEquals("Beinn Tulaichean", munroDataResults[1].name)
        assertEquals(945.8, munroDataResults[1].heightInMeters)
        assertEquals("MUN", munroDataResults[1].hillCategory)
        assertEquals("NN416196", munroDataResults[1].gridRef)

        assertEquals("Sgurr na Sgine", munroDataResults[2].name)
        assertEquals(946.0, munroDataResults[2].heightInMeters)
        assertEquals("MUN", munroDataResults[2].hillCategory)
        assertEquals("NG946113", munroDataResults[2].gridRef)
    }



}