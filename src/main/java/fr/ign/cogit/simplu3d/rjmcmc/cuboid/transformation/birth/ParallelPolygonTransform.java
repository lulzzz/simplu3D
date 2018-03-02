package fr.ign.cogit.simplu3d.rjmcmc.cuboid.transformation.birth;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.util.conversion.AdapterFactory;
import fr.ign.geometry.transform.PolygonTransform;
import fr.ign.rjmcmc.kernel.Transform;

/**
 * 
 * This software is released under the licence CeCILL
 * 
 * see LICENSE.TXT
 * 
 * see <http://www.cecill.info/ http://www.cecill.info/
 * 
 * @copyright IGN
 * 
 * @version 1.0
 **/
public class ParallelPolygonTransform implements Transform {
  /**
   * Logger.
   */
  static Logger LOGGER = Logger.getLogger(ParallelPolygonTransform.class.getName());

  private double absJacobian[];
  private PolygonTransform polygonTransform;
//  private MultiLineString limits;
  private GeometryFactory factory = new GeometryFactory();

  private double deltaLength;
  private double deltaHeight;
  private double rangeLength;
  private double rangeHeight;
  
  private boolean isValid = true;

  public ParallelPolygonTransform(double[] d, double[] v, IGeometry polygon)
      throws Exception {
    this.rangeLength = d[2];
    this.rangeHeight = d[4];
    this.deltaLength = v[2];
    this.deltaHeight = v[4];
    double determinant = rangeLength * rangeHeight;
//    LineString[] lineStrings = new LineString[limits.length];
//    for (int i = 0; i < limits.length; i++) {
//      lineStrings[i] = (LineString) AdapterFactory.toGeometry(factory, limits[i]);
//    }
//    this.limits = factory.createMultiLineString(lineStrings);
    Geometry pp = AdapterFactory.toGeometry(factory, polygon);
    this.polygonTransform = new PolygonTransform(pp, 0.1);
    
    
    isValid = this.polygonTransform.isValid();
    
    
    this.absJacobian = new double[2];
    this.absJacobian[0] = Math.abs(determinant) * this.polygonTransform.getAbsJacobian(true);
    this.absJacobian[1] = Math.abs(1 / determinant) * this.polygonTransform.getAbsJacobian(false);
  }
  
  /**
   * Indicate if the transform is valid (i.e: that the triangulation in the PolygonTransform is ok)
   * @return
   */
  public boolean isValid(){  
	  return isValid;
  }

  @Override
  public double apply(boolean direct, double[] val0, double[] val1) {
    double pt = this.polygonTransform.apply(direct, val0, val1);
    if (direct) {
//      Coordinate p = new Coordinate(val1.get(0), val1.get(1));
//      DistanceOp op = new DistanceOp(this.limits, factory.createPoint(p));
//      Coordinate projected = op.nearestPoints()[0];
//      double distance = op.distance();
//      double orientation = Angle.angle(p, projected);
      val1[2] = val0[2] * rangeLength + deltaLength;
//      val1.set(3, distance * 2);
      val1[3] = val0[3] * rangeHeight + deltaHeight;
//      val1.set(5, orientation + Math.PI / 2);
      return pt * this.absJacobian[0];
    } else {
      val1[2] = (val0[2] - deltaLength) / rangeLength;
      val1[3] = (val0[3] - deltaHeight) / rangeHeight;
//      var1.set(4, 0.0);
//      var1.set(5, 0.0);
      return pt * this.absJacobian[1];
    }
  }

//  @Override
  public double getAbsJacobian(boolean direct) {
    return this.absJacobian[direct ? 0 : 1];
  }

  @Override
  public int dimension() {
    return 4;
  }
}