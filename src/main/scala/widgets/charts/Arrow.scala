package widgets.charts

import threejs.{BufferAttribute, BufferGeometry, Group, Line, LineBasicMaterial, LineMaterialParameters,  Vector3}

import scala.scalajs.js.typedarray.Float32Array

class Arrow(position: Vector3)  extends Group() {

  def init(): Unit = {
    val geometry = new BufferGeometry()
    val numPoints = 3
    val radius = 1
    val positions = new Float32Array(numPoints * 3)
    for (p <- 0 until numPoints) {
      positions(p * 3) = /*position.x + */ - radius + p * radius // x
      positions(p * 3 + 1) = /*position.y */ - radius + (p % 2) * radius // y
      positions(p * 3 + 2) = /*position.z*/ 0 // z
    }
    geometry.setAttribute( "position", new BufferAttribute( positions, 3 ) )
    val obj = new Line( geometry, new LineBasicMaterial( LineMaterialParameters("#ffff00") ) )
    this.add(obj)
  }

  init()
}
