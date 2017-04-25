package com.br.reader
import com.br.model.InvestorOrder
/**
 * The facade which would take the file and depending upon the file type invoke the appropriate Reader
 * If more file types need to be handled -this class can iclude cases for the same
 */
class InvestorFacade(){

 def readInvestorData(fileName:String):Seq[InvestorOrder] = {
   
 var investorOrders =   fileName match {
   case f if(fileName.endsWith(".csv")) => parseCsvFile(fileName)
   case f if(fileName.endsWith(".jsons")) => parseJsonFile(fileName)  
   case f => throw new RuntimeException("unknown file format")  
   }
 investorOrders
 }
  
 def parseCsvFile(fileName:String):Seq[InvestorOrder] ={
   InvestorCSVReader(fileName).readInvestorOrders()
 }
 
 def parseJsonFile(fileName:String):Seq[InvestorOrder] ={
   InvestorJSONReader(fileName).readInvestorOrders()
 }
}
object InvestorFacade {
 def apply()={
    new InvestorFacade()
  }
}