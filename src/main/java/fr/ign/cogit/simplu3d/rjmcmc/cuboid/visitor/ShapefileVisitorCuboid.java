package fr.ign.cogit.simplu3d.rjmcmc.cuboid.visitor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.util.conversion.JtsGeOxygene;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.convert.GenerateSolidFromCuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.mpp.configuration.GraphConfiguration;
import fr.ign.rjmcmc.configuration.Configuration;
import fr.ign.rjmcmc.kernel.SimpleObject;
import fr.ign.rjmcmc.sampler.Sampler;
import fr.ign.simulatedannealing.temperature.Temperature;
import fr.ign.simulatedannealing.visitor.Visitor;

public class ShapefileVisitorCuboid<O extends SimpleObject> implements Visitor<O> {
  private int save;
  private int iter;
  private String fileName;

  public ShapefileVisitorCuboid(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void init(int dump, int s) {
    this.iter = 0;
    this.save = s;
  }

  @Override
  public void begin(Configuration<O> config, Sampler<O> sampler, Temperature t) {
  }

  @SuppressWarnings({ "unchecked" })
  @Override
  public void end(Configuration<O> config, Sampler<O> sampler, Temperature t) {
    this.writeShapefile(fileName + "_" + String.format(formatInt, iter + 1) + ".shp",
        (GraphConfiguration<Cuboid>) config);
  }

  String formatInt = "%1$-10d";

  @SuppressWarnings({ "unchecked" })
  @Override
  public void visit(Configuration<O> config, Sampler<O> sampler, Temperature t) {
    ++iter;
    if ((save > 0) && (iter % save == 0)) {
      this.writeShapefile(fileName + "_" + String.format(formatInt, iter) + ".shp",
          (GraphConfiguration<Cuboid>) config);
    }
  }

  @SuppressWarnings({ "unchecked", "deprecation" })
  private void writeShapefile(String aFileName, GraphConfiguration<Cuboid> config) {
    try {
      ShapefileDataStore store = new ShapefileDataStore(new File(aFileName).toURI().toURL());
      String specs = "geom:MultiPolygon,energy:double"; //$NON-NLS-1$
      String featureTypeName = "Building"; //$NON-NLS-1$
      SimpleFeatureType type = DataUtilities.createType(featureTypeName, specs);
      store.createSchema(type);
      FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) store
          .getFeatureSource(featureTypeName);
      Transaction transaction = new DefaultTransaction();
      FeatureCollection<SimpleFeatureType, SimpleFeature> collection = FeatureCollections
          .newCollection();
      int i = 1;
      GraphConfiguration<Cuboid> graph = (GraphConfiguration<Cuboid>) config;
      for (GraphConfiguration<Cuboid>.GraphVertex v : graph.getGraph().vertexSet()) {

        List<Object> liste = new ArrayList<>();

        IMultiSurface<IOrientableSurface> iMS = new GM_MultiSurface<>();
        iMS.addAll(GenerateSolidFromCuboid.generate(v.getValue()).getFacesList());

        liste.add(JtsGeOxygene.makeJtsGeom(iMS));
        liste.add(v.getEnergy());
        SimpleFeature simpleFeature = SimpleFeatureBuilder.build(type, liste.toArray(),
            String.valueOf(i++));
        collection.add(simpleFeature);
      }
      featureStore.addFeatures(collection);
      transaction.commit();
      transaction.close();
      store.dispose();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (SchemaException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
