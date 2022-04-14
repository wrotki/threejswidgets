package widgets.charts

import objects3d.{Component, Update}
import threejs.{BufferAttribute, BufferGeometry, Group, Line, LineBasicMaterial, LineMaterialParameters, Quaternion, Vector3}

import scala.scalajs.js.typedarray.Float32Array

// https://github.com/mrdoob/three.js/blob/master/examples/misc_exporter_gltf.html
// THREE.Line Strip
// THREE.Line Loop

class Axis(vec: Vector3) extends Group() with Update {
  // TODO components and update should likely be in some base trait
  var components = Seq.empty[Component]

  def update: Unit = {
    for (c <- components)
      c.update()
  }

  def init(): Unit = {
    val geometry = new BufferGeometry()
    val numPoints = 2
    val radius = math.max(math.max(vec.x, vec.y),vec.z)
    val positions = new Float32Array(numPoints * 3)
    for (p <- 0 until numPoints) {
      positions(p * 3) = 0
      positions(p * 3 + 1) = p * radius
      positions(p * 3 + 2) = 0
    }
    geometry.setAttribute( "position", new BufferAttribute( positions, 3 ) )
    val obj = new Line( geometry, new LineBasicMaterial( LineMaterialParameters("#ffff00") ) )
    this.add(obj)

    val arrow = new Arrow(new Vector3(0, radius, 0))
    arrow.position.set(0f,radius ,0f)
    this.add(arrow)

    val label = new Label()
    label.position.set(0f, 200f ,0f)
    this.add(label)
  }

  def rotate(): Unit = {
    val id = new Quaternion(0f,0f,0f,0f).identity()
    var updateQuaternion = id.setFromAxisAngle(new Vector3(0f,1f,0f), (Math.PI/2.0).toFloat)
    if( vec.x > 0.0f ) {
      id.setFromAxisAngle(new Vector3(0f,0f,-1f), (Math.PI/2.0).toFloat)
    }
    if( vec.z > 0.0f ) {
      id.setFromAxisAngle(new Vector3(1f,0f,0f), (Math.PI/2.0).toFloat)
    }
    if( vec.y == 0.0f ) { // Y axis is already in place
      val newOwnerQuaternion = this.quaternion.clone()
      newOwnerQuaternion.multiply(updateQuaternion)
      newOwnerQuaternion.normalize()
      this.quaternion.copy(newOwnerQuaternion)
    }
  }

  init()
  rotate()
}
