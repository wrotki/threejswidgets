package objects3d

import threejs.Mesh

import scala.scalajs.js

abstract class Component(owner: Mesh) extends js.Object with Update {

}
