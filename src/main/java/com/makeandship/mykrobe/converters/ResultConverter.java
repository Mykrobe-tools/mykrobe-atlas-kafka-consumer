package com.makeandship.mykrobe.converters;

import java.util.List;

import com.makeandship.mykrobe.Constants;
import com.makeandship.mykrobe.models.DistanceResult;
import com.makeandship.mykrobe.models.ExperimentResult;
import com.makeandship.mykrobe.models.MykrobePhylogenetics;
import com.makeandship.mykrobe.models.MykrobeSusceptibility;
import com.makeandship.mykrobe.models.PredictorResult;

public class ResultConverter {

	public static PredictorResult toPredictorResult(String oid, ExperimentResult result) {
		PredictorResult predictor = new PredictorResult();
		predictor.setExperimentId(oid);
		predictor.setKmer(result.getKmer());
		predictor.setExternalId(result.getExternalId());
		predictor.setGenotypeModel(result.getGenotypeModel());
		if (result.getVersion() != null) {
			predictor.setMykrobeAtlasVersion(result.getVersion().getMykrobeAtlas());
			predictor.setMykrobePredictorVersion(result.getVersion().getMykrobePredictor());
		}
		predictor.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
		List<MykrobeSusceptibility> susceptibilityList = result.getSusceptibility();
		if (susceptibilityList != null) {
			for (MykrobeSusceptibility susceptibility : susceptibilityList) {
				switch (susceptibility.getName()) {
				case Constants.ISONIAZID:
					predictor.setIsoniazid(susceptibility.getPrediction());
					break;
				case Constants.KANAMYCIN:
					predictor.setKanamycin(susceptibility.getPrediction());
					break;
				case Constants.ETHAMBUTOL:
					predictor.setEthambutol(susceptibility.getPrediction());
					break;
				case Constants.STREPTOMYCIN:
					predictor.setStreptomycin(susceptibility.getPrediction());
					break;
				case Constants.CAPREOMYCIN:
					predictor.setCapreomycin(susceptibility.getPrediction());
					break;
				case Constants.CIPROFLOXACIN:
					predictor.setCiprofloxacin(susceptibility.getPrediction());
					break;
				case Constants.MOXIFLOXACIN:
					predictor.setMoxifloxacin(susceptibility.getPrediction());
					break;
				case Constants.OFLOXACIN:
					predictor.setOfloxacin(susceptibility.getPrediction());
					break;
				case Constants.AMIKACIN:
					predictor.setAmikacin(susceptibility.getPrediction());
					break;
				case Constants.PYRAZINAMIDE:
					predictor.setPyrazinamide(susceptibility.getPrediction());
					break;
				case Constants.RIFAMPICIN:
					predictor.setRifampicin(susceptibility.getPrediction());
					break;
				}

			}
		}
		
		List<MykrobePhylogenetics> phylogeneticsList = result.getPhylogenetics();
		if (phylogeneticsList != null) {
			for (MykrobePhylogenetics phylogenetics : phylogeneticsList) {
				String phylogeneticsResult = phylogenetics.getResult();
				float phylogeneticsPercentCoverage = phylogenetics.getPercentCoverage();
				int phylogeneticsMedianDepth = phylogenetics.getMedianDepth();
			
				switch (phylogenetics.getType()) {
				case Constants.PHYLO_GROUP:
					predictor.setPhyloGroup(phylogeneticsResult);
					predictor.setPhyloGroupPercentCoverage(phylogeneticsPercentCoverage);
					predictor.setPhyloGroupMedianDepth(phylogeneticsMedianDepth);
					break;
				case Constants.SUB_COMPLEX: 
					predictor.setSubComplex(phylogeneticsResult);
					predictor.setSubComplexPercentCoverage(phylogeneticsPercentCoverage);
					predictor.setSubComplexMedianDepth(phylogeneticsMedianDepth);
					break;
				case Constants.SPECIES:
					predictor.setSpecies(phylogeneticsResult);
					predictor.setSpeciesPercentCoverage(phylogeneticsPercentCoverage);
					predictor.setSpeciesMedianDepth(phylogeneticsMedianDepth);
					break;
				case Constants.LINEAGE:
					predictor.setLineage(phylogeneticsResult);
					predictor.setLineagePercentCoverage(phylogeneticsPercentCoverage);
					predictor.setLineageMedianDepth(phylogeneticsMedianDepth);
					break;
				}
			}
		}

		predictor.setR(result.isR());
		predictor.setMdr(result.isMdr());
		predictor.setXdr(result.isXdr());
		predictor.setTdr(result.isTdr());

		return predictor;
	}

	public static DistanceResult toDistanceResult(String oid, ExperimentResult result) {
		DistanceResult distance = new DistanceResult();
		distance.setExperimentId(oid);
		distance.setReceived(DateConverter.dateToMysql(result.getReceived().getDate()));
		distance.setSubType(result.getSubType());
		distance.setType(result.getType());
		return distance;
	}

}
