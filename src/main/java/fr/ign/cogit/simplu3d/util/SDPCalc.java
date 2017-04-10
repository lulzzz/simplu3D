package fr.ign.cogit.simplu3d.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.AbstractSimpleBuilding;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid;

/**
 * Computes the "Surface de plancher" from a collection of cuboids, essentially
 * the ground surface x nb of floors. It does it by building a partition from
 * the intersections of the cuboids, associating the correct height to each
 * part, and finally summing them
 * @author imran
 *
 */
public class SDPCalc {

  private double FLOOR_HEIGHT = 3;

  public SDPCalc() {
  }

  public SDPCalc(double floorHeight) {
    this.FLOOR_HEIGHT = floorHeight;
  }

  /**
   * 
   * structure to combine a surface and its associated height
   *
   */
  private class GeomHeightPair {
    public double height;
    public Geometry geom;

    public GeomHeightPair(Geometry g, double h) {
      this.height = h;
      this.geom = g;
    }

    public double sdp() {
      double epsilon = 0.01;
      // if height is x.99 we want it to be x+1
      if (height - ((int) (height)) > (1 - epsilon)) {
        height = (int) (height) + 1;
      }
      return geom.getArea() * (Math.floor(height / FLOOR_HEIGHT));
    }

  }

  public double process(String shape) {
    List<Cuboid> lCuboid = LoaderCuboid.loadFromShapeFile(shape);
    return process(lCuboid);
  }

  public double process(List<Cuboid> cubes) {
    double sdp = 0;
    List<List<AbstractSimpleBuilding>> lGroupes = CuboidGroupCreation
        .createGroup(cubes, 0);
    System.out.println("nb groupes formé " + lGroupes.size());
    for (List<AbstractSimpleBuilding> g : lGroupes)
      sdp += sdpGroup(g);
    return sdp;
  }

  private double sdpGroup(List<AbstractSimpleBuilding> group) {
    List<GeomHeightPair> aPrec = new ArrayList<>();
    System.out.println("processing group with size " + group.size());
    AbstractSimpleBuilding cuboid = group.remove(0);
    aPrec.add(new GeomHeightPair(cuboid.toGeometry(), cuboid.height));
    for (AbstractSimpleBuilding b : group) {
      List<GeomHeightPair> aCurrent = new ArrayList<>();
      Geometry bgeom = b.toGeometry();
      for (GeomHeightPair a : aPrec) {
        Geometry diff = a.geom.difference(bgeom);
        aCurrent.add(new GeomHeightPair(diff, a.height));
        Geometry inter = a.geom.intersection(bgeom);
        aCurrent.add(new GeomHeightPair(inter, Math.max(a.height, b.height)));
        // System.out
        // .println("aprec [" + aPrec.size() + "] " + getUnionGeom(aPrec));
      }
      Geometry bdiffAPrec = bgeom.difference(getUnionGeom(aPrec));
      aCurrent.add(new GeomHeightPair(bdiffAPrec, b.height));
      aPrec = aCurrent;
    }
    double sdp = 0;
    for (GeomHeightPair e : aPrec) {
      // System.out.println("sdp partiel " + e.sdp());
      sdp += e.sdp();
      // System.out.println(e.height + " -- " + e.geom);
    }
    return sdp;
  }

  private Geometry getUnionGeom(List<GeomHeightPair> aPrec) {
    Collection<Geometry> collGeom = new ArrayList<>();
    for (GeomHeightPair e : aPrec) {
      collGeom.add(e.geom);
    }
    Geometry union = CascadedPolygonUnion.union(collGeom);
    return union;
  }

  public static void main(String[] args) {
    SDPCalc sd = new SDPCalc();
    int imu = 77017278;
    double sdp = sd.process("/home/imran/testoss/EPFIF/outCapaBig/res/" + imu
        + "/simulations/simul_" + imu + "_true_no_demo_sampler.shp");
    System.out.println("SDP :" + sdp);
  }
}