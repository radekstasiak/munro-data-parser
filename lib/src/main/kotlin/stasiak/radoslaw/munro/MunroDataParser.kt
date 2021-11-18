package stasiak.radoslaw.munro

import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MunroDataParser(inputStream: FileInputStream) {
    val list: ArrayList<String> = arrayListOf()

    init {
        try {
            val scanner = Scanner(inputStream, "UTF-8")
            while (scanner.hasNextLine()) {
                val line: String = scanner.nextLine()
                list.add(line)
            }

            if (scanner.ioException() != null) {
                throw scanner.ioException()
            }

            scanner.close()
        } catch (exception: IOException) {

//        } finally {
//            inputStream.close()
//            if (sca != null) {
//                sc.close()
//            }
//        }
        }


    }

    fun getResults(): List<String> = list
}