package fr.ign.cogit.appli.xdogs.theseMickael.indicators.oneBuildingPart.dim2;

import fr.ign.cogit.appli.xdogs.theseMickael.indicators.indicatorSchem.oneBuildingPart.SingleBIndicator;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.sig3d.calculation.OrientedBoundingBox;
import fr.ign.cogit.sig3d.convert.decomposition.Building;

public class Largeur extends SingleBIndicator {
  
  private double value;

  public Largeur(Building b) {
    super(b);
    
    
    OrientedBoundingBox Bb = new OrientedBoundingBox(b.getGeom());
    
    
    IPolygon pol = Bb.getPoly();
    
    IDirectPositionList dpl = pol.coord();
    
    
    double largeur = dpl.get(0).distance(dpl.get(1));
    
    
    value = Math.min(largeur,  dpl.get(2).distance(dpl.get(1)));
    
    
   
  }


  @Override
  public Double getValue() {
    // TODO Auto-generated method stub
    return value;
  }

  public String getType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.NAME_NUMERIC;
  }

  public String getAttributeName() {
    // TODO Auto-generated method stub
    return "Largeur";
  }

  @Override
  public String getAggregationType() {
    // TODO Auto-generated method stub
    return SingleBIndicator.AGG_MOY;
  }


}
