package com.br.model
import org.joda.time.DateTime;
/**
 * The class representing the Investor order
 */
case class InvestorOrder(investorId: Long, invType: String, date: DateTime, ticker: String, amount: Long) extends Ordered[InvestorOrder] {
  // return 0 if the same, negative if this < that, positive if this > that

  def compare(that: InvestorOrder): Int = {
    if (this.date.isBefore(that.date))
      -1
    else if (this.date.isAfter(that.date))
      1
    else
      0
  }

}