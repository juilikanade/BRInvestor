package com.br.reader
import com.br.model.InvestorOrder

/**
  * Trait responsible for reading/loading [[Investor]].
  *
  * @author jkanade
  */
trait InvestorReader {

  /**
    * @return A [[Seq]] containing all the investor.
    */
  def readInvestorOrders(): Seq[InvestorOrder]

}

