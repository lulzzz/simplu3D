package fr.ign.cogit.simplu3d.model.application;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.sig3d.model.citygml.building.CG_RoofSurface;
import fr.ign.cogit.simplu3d.calculation.RoofAngle;

public class RoofSurface extends CG_RoofSurface {

  public IMultiCurve<IOrientableCurve> gutter;
  public IMultiCurve<IOrientableCurve> gable;
  public IMultiCurve<IOrientableCurve> roofing;
  public IMultiCurve<IOrientableCurve> interiorEdge;

  public IMultiCurve<IOrientableCurve> getInteriorEdge() {
    return interiorEdge;
  }

  public void setInteriorEdge(IMultiCurve<IOrientableCurve> interiorEdge) {
    this.interiorEdge = interiorEdge;
  }

  private Materiau material;

  private int numberOfSlopes;

  public IMultiCurve<IOrientableCurve> setGutter() {
    return gutter;
  }

  public IMultiCurve<IOrientableCurve> setGable() {
    return gable;
  }

  public void setGable(IMultiCurve<? extends IOrientableCurve> pignons) {
    this.gable = new GM_MultiCurve<IOrientableCurve>();
    this.gable.addAll(pignons);
  }

  public IMultiCurve<IOrientableCurve> getRoofing() {
    return roofing;
  }

  public void setRoofing(IMultiCurve<? extends IOrientableCurve> faitage) {
    this.roofing = new GM_MultiCurve<IOrientableCurve>();
    this.roofing.addAll(faitage);
  }

  public int getNBSlopes() {
    return numberOfSlopes;
  }

  public void setNBSlopes(int nbPans) {
    this.numberOfSlopes = nbPans;
  }

  public Materiau getMat() {
    return material;
  }

  public void setMat(Materiau mat) {
    this.material = mat;
  }

  public void setGutter(
      IMultiCurve<? extends IOrientableCurve> ligneGoutierre) {
    this.gutter = new GM_MultiCurve<IOrientableCurve>();
    this.gutter.addAll(ligneGoutierre);

  }

  public Object clone() {
    RoofSurface tCopy = new RoofSurface();

    tCopy.setGeom((IGeometry) this.getGeom().clone());
    tCopy.setLod2MultiSurface((IMultiSurface<IOrientableSurface>) this
        .getGeom().clone());
    tCopy.setGutter((IMultiCurve<IOrientableCurve>) this.setGutter()
        .clone());
    tCopy.setRoofing((IMultiCurve<IOrientableCurve>) this.getRoofing().clone());

    return tCopy;

  }


  private double angleMin = Double.NaN;
  private double angleMax = Double.NaN;
  
  
  
  public double getAngleMin() {
    
    
    if(Double.isNaN(angleMin)){
      angleMin = RoofAngle.angleMin(this);
    }
    
    return angleMin;
  }

  public void setAngleMin(double angleMin) {
    this.angleMin = angleMin;
  }

  public double getAngleMax() {
    
    if(Double.isNaN(angleMax)){
      angleMax = RoofAngle.angleMax(this);
    }
    
    return angleMax;
  }

  public void setAngleMax(double angleMax) {
    this.angleMax = angleMax;
  }
  

}
