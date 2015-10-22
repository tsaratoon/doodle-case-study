package doodle.core.tsaratoon

case class BoundingBox(left: Double, right: Double, top: Double, bottom: Double){
  
   val width = right - left
   val height = top - bottom
   
   def on(that: BoundingBox): BoundingBox = {
     val minLeft = this.left min that.left
     val minRight = this.right min that.right
     val minTop = this.top min that.top
     val minBottom = this.bottom min that.bottom
     BoundingBox(minLeft, minRight, minTop, minBottom)
   }
   
   def above(that: BoundingBox): BoundingBox =
     BoundingBox(left, right, that.top, bottom)
   
   def beside(that: BoundingBox): BoundingBox = {
     val minTop = this.top min that.top
     val minBottom = this.bottom min that.bottom
     BoundingBox(left, that.right, minTop, minBottom)
   }
     
}