package com.br.app
import scala.collection.immutable.ListMap

import com.br.reader._
import com.br.model.InvestorOrder
import com.br.stats.InvestorMetrics

/** 
 *  The main class which prints out the Investor metrics
 **/
object InvestorMetricFeed {
  
 var investorMetrics = InvestorMetrics()
 
 def main ( args: Array[String]) {

    //Fetch the data
    var fileNames = Seq[String]("/investor_data.csv", "/investor_data.jsons")
    var listInvestorData: Seq[InvestorOrder] = List.empty
    fileNames.foreach { fileName =>
      {
        listInvestorData = listInvestorData ++ InvestorFacade().readInvestorData(fileName)
      }
    }

   
   //print out the various metrics
   
   printAmountRangeForInvestor(listInvestorData:Seq[InvestorOrder])
   
   var activeInvestors = investorMetrics.getActiveInvestors(listInvestorData)  
   println("The active investors are "+activeInvestors.mkString(","))
   
   var buyAndHoldInvestors = investorMetrics.getBuyAndHoldInvestors(listInvestorData)  
   println("The buy and hold investors are "+buyAndHoldInvestors.mkString(","))
     
   var mostBoughtTicker = investorMetrics.getMostBoughtTicker(listInvestorData)
   println("The most bought security was :"+mostBoughtTicker.ticker)
   
    var mostSoldTicker = investorMetrics.getMostSoldTicker(listInvestorData)
   println("The most sold security was :"+mostSoldTicker.ticker)
   
  }
 
 /**
  * Print the largest and smallest order by amount for each investor
  */
 def  printAmountRangeForInvestor(listInvestorData:Seq[InvestorOrder])={
     var mapInvestorToOrders = listInvestorData.groupBy(_.investorId)
     var mapInvestorToOrdersSorted = ListMap(mapInvestorToOrders.toSeq.sortBy(_._1):_*)//sort by investor id
     
     
     mapInvestorToOrdersSorted.keys.foreach(investorId => { 
         var investorOrderLargest = investorMetrics.getLargestTradeForInvestor(mapInvestorToOrders(investorId),investorId)
         var investorOrderSmallest = investorMetrics.getSmallestTradeForInvestor(mapInvestorToOrders(investorId),investorId)
         println(getInvestorMessage(investorOrderLargest, investorOrderSmallest))     
     })    
 }

 /**
  * Method which returns the investor amount range
  */
 def  getInvestorMessage(investorOrderLargest:InvestorOrder, investorOrderSmallest:InvestorOrder):String = {
  
   val builder = StringBuilder.newBuilder
    builder.append("Investor ")
    builder.append(investorOrderLargest.investorId)
    builder.append("'s")
    builder.append(" largest trade was ")
    builder.append(investorOrderLargest.invType)
    builder.append(" ")
    builder.append(investorOrderLargest.amount)
    builder.append(" of ")
    builder.append(investorOrderLargest.ticker)
    builder.append(" and smallest trade was ")
    builder.append(investorOrderSmallest.invType)
    builder.append(" ")
    builder.append(investorOrderSmallest.amount)
    builder.append(" of ")
    builder.append(investorOrderSmallest.ticker)
    
    builder.toString()
   
 }

}