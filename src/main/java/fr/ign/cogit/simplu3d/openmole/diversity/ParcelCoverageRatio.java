package fr.ign.cogit.simplu3d.openmole.diversity;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.algo.JtsAlgorithms;
import fr.ign.cogit.simplu3d.io.ExportAsFeatureCollection;
import fr.ign.cogit.simplu3d.model.application.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.GraphConfiguration;

public class ParcelCoverageRatio {
  GraphConfiguration<Cuboid> conf;
  BasicPropertyUnit propertyUnit;

  public ParcelCoverageRatio(GraphConfiguration<Cuboid> c, BasicPropertyUnit bPU) {
    this.conf = c;
    this.propertyUnit = bPU;
  }

  @SuppressWarnings("unchecked")
  public double getCoverageRatio() {
    ExportAsFeatureCollection exporter = new ExportAsFeatureCollection(this.conf);
    IFeatureCollection<IFeature> collection = exporter.getFeatureCollection();
    double area = this.propertyUnit.getGeom().area();
    List<GM_MultiSurface<IPolygon>> list = new ArrayList<>();
    for (IFeature f : collection) {
      list.add((GM_MultiSurface<IPolygon>) f.getGeom());
    }
    double buildingArea = JtsAlgorithms.union(list).area();
    return buildingArea / area;
  }
}
