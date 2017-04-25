package com.br.util
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Utility class to handle date in CSV and JSON
 */

object DateUtil {
  
  def toDateTime( date:String): DateTime = {
   DateTime.parse(date,DateTimeFormat.forPattern("MM/dd/yyyy"))
    }

  def toDateTimeForDashFormat( date:String): DateTime = {
   DateTime.parse(date,DateTimeFormat.forPattern("MM-dd-yyyy"))
    }
}