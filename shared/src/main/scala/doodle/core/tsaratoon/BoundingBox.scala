package doodle.core.tsaratoon

import doodle.core.Vec

case class BoundingBox(left: Double, top: Double, right: Double, bottom: Double){
  val width = right - left
  val height = top - bottom
   
  def on(that: BoundingBox): BoundingBox = {
    val left = this.left min that.left
    val right = this.right max that.right
    val top = this.top max that.top
    val bottom = this.bottom min that.bottom
    BoundingBox(left, top, right, bottom)
  }
   
  def beside(that: BoundingBox): BoundingBox = {
	  val top = this.top max that.top
		val bottom = this.bottom min that.bottom
		val left = -(this.width + that.width) / 2
		val right = (this.width + that.width) / 2
		BoundingBox(left, top, right, bottom)
  }
   
  def above(that: BoundingBox): BoundingBox = {
    val left = this.left min that.left
    val right = this.right max that.right
    val top = (this.height + that.height) / 2
    val bottom = -(this.height + that.height) / 2
    BoundingBox(left, top, right, bottom)
  }
  
  def translate(v: Vec): BoundingBox =
    BoundingBox(left + v.x, top + v.y, right + v.x, bottom + v.y)
     
}