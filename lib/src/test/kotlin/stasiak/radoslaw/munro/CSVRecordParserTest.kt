package stasiak.radoslaw.munro

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CSVRecordParserTest {


    @Test
    fun `test regular value is parsed correctly`() {
        val csvRecord = "test value"

        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result
        assertTrue(result.size == 1)
        assertEquals("test value", result[0])

    }

    @Test
    fun `test quoted value is parsed correctly`() {
        val csvRecord = "\"quoted \"\" field\""

        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result
        assertTrue(result.size == 1)
        assertEquals("quoted \" field", result[0])

    }

    @Test
    fun `test entry starting with an empty field and ending with a quoted value is parsed correctly`() {
        val csvRecord =
            ",1,\"http://www.streetmap.co.uk/newmap.srf?x=277324&y=730857&z=3&sv=277324,730857&st=4&tl=~&bi=~&lu=N&ar=y\",http://www.geograph.org.uk/gridref/NN7732430857,http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=1,Ben Chonzie,1,01A,1.1,931,3054,51 52,OL47W 368W 379W,NN773308,NN7732430857,277324,730857,MUN,,TOP,\"Hej, to jest \"\" test\""

        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result

        assertEquals("", result[0])
        assertEquals("1", result[1])
        assertEquals(
            "http://www.streetmap.co.uk/newmap.srf?x=277324&y=730857&z=3&sv=277324,730857&st=4&tl=~&bi=~&lu=N&ar=y",
            result[2]
        )
        assertEquals("http://www.geograph.org.uk/gridref/NN7732430857", result[3])
        assertEquals("http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=1", result[4])
        assertEquals("Ben Chonzie", result[5])
        assertEquals("1", result[6])
        assertEquals("01A", result[7])
        assertEquals("1.1", result[8])
        assertEquals("931", result[9])
        assertEquals("3054", result[10])
        assertEquals("51 52", result[11])
        assertEquals("OL47W 368W 379W", result[12])
        assertEquals("NN773308", result[13])
        assertEquals("NN7732430857", result[14])
        assertEquals("277324", result[15])
        assertEquals("730857", result[16])
        assertEquals("MUN", result[17])
        assertEquals("", result[18])
        assertEquals("TOP", result[19])
        assertEquals("Hej, to jest \" test", result[20])

        assertEquals(21, result.size)
    }

    @Test
    fun `test entry starting with a regular value and ending with an empty field is parsed correctly`() {
        val csvRecord =
            "503,934,\"http://www.streetmap.co.uk/newmap.srf?x=211733&y=833773&z=3&sv=211733,833773&st=4&tl=~&bi=~&lu=N&ar=y\",http://www.geograph.org.uk/gridref/NH1173333773,http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=934,An Riabhachan West Top,12,12B,12.2,1038,3406,25,429 430,NH117337,NH1173333773,211733,833773,TOP,,,"

        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result

        assertEquals("503", result[0])
        assertEquals("934", result[1])
        assertEquals(
            "http://www.streetmap.co.uk/newmap.srf?x=211733&y=833773&z=3&sv=211733,833773&st=4&tl=~&bi=~&lu=N&ar=y",
            result[2]
        )
        assertEquals("http://www.geograph.org.uk/gridref/NH1173333773", result[3])
        assertEquals("http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=934", result[4])
        assertEquals("An Riabhachan West Top", result[5])
        assertEquals("12", result[6])
        assertEquals("12B", result[7])
        assertEquals("12.2", result[8])
        assertEquals("1038", result[9])
        assertEquals("3406", result[10])
        assertEquals("25", result[11])
        assertEquals("429 430", result[12])
        assertEquals("NH117337", result[13])
        assertEquals("NH1173333773", result[14])
        assertEquals("211733", result[15])
        assertEquals("833773", result[16])
        assertEquals("TOP", result[17])
        assertEquals("", result[18])
        assertEquals("", result[19])
        assertEquals("", result[20])

        assertEquals(21, result.size)
    }

    @Test
    fun `test entry starting with a quoted value and ending with regular field is parsed correctly`() {
        val csvRecord =
            "\"\"quoted, field\",982,\"http://www.streetmap.co.uk/newmap.srf?x=192406&y=849911&z=3&sv=192406,849911&st=4&tl=~&bi=~&lu=N&ar=y\",http://www.geograph.org.uk/gridref/NG9240649911,http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=982,Maol Chean-dearg,13,13B,13.2,933,3061,25,429,NG924499,NG9240649911,192406,849911,,,MUN,1891: Meall 'Chinn Dearg"
        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result

        assertEquals("quoted, field", result[0])
        assertEquals("982", result[1])
        assertEquals(
            "http://www.streetmap.co.uk/newmap.srf?x=192406&y=849911&z=3&sv=192406,849911&st=4&tl=~&bi=~&lu=N&ar=y",
            result[2]
        )
        assertEquals("http://www.geograph.org.uk/gridref/NG9240649911", result[3])
        assertEquals("http://www.hill-bagging.co.uk/mountaindetails.php?qu=S&rf=982", result[4])
        assertEquals("Maol Chean-dearg", result[5])
        assertEquals("13", result[6])
        assertEquals("13B", result[7])
        assertEquals("13.2", result[8])
        assertEquals("933", result[9])
        assertEquals("3061", result[10])
        assertEquals("25", result[11])
        assertEquals("429", result[12])
        assertEquals("NG924499", result[13])
        assertEquals("NG9240649911", result[14])
        assertEquals("192406", result[15])
        assertEquals("849911", result[16])
        assertEquals("", result[17])
        assertEquals("", result[18])
        assertEquals("MUN", result[19])
        assertEquals("1891: Meall 'Chinn Dearg", result[20])

        assertEquals(21, result.size)
    }

    @Test
    fun `test entry with all fields empty is parsed correctly`() {
        val csvRecord = ",,,,,,,,,,,,,,,,,,,,,,,,,,,,"
        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result

        assertEquals(29, result.size)
        for (item in result) {
            assertTrue(item.isEmpty())
        }
    }

    @Test
    fun `test entry consisting of only 2 empty fields is parsed correctly`() {
        val csvRecord = ","
        val csvRecordParser = CSVRecordParser(csvRecord = csvRecord, ",".single())
        val result = csvRecordParser.result

        assertEquals(2, result.size)
        assertEquals("", result[0])
        assertEquals("", result[1])
    }
}