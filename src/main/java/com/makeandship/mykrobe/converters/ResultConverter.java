package com.makeandship.mykrobe.converters;

import java.util.List;

import com.makeandship.mykrobe.Constants;
import com.makeandship.mykrobe.models.ExperimentResult;
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
				case Constants.KANAMYCIN:
					predictor.setKanamycin(susceptibility.getPrediction());
				case Constants.ETHAMBUTOL:
					predictor.setEthambutol(susceptibility.getPrediction());
				case Constants.STREPTOMYCIN:
					predictor.setStreptomycin(susceptibility.getPrediction());
				case Constants.CAPREOMYCIN:
					predictor.setCapreomycin(susceptibility.getPrediction());
				case Constants.CIPROFLOXACIN:
					predictor.setCiprofloxacin(susceptibility.getPrediction());
				case Constants.MOXIFLOXACIN:
					predictor.setMoxifloxacin(susceptibility.getPrediction());
				case Constants.OFLOXACIN:
					predictor.setOfloxacin(susceptibility.getPrediction());
				case Constants.AMIKACIN:
					predictor.setAmikacin(susceptibility.getPrediction());
				case Constants.PYRAZINAMIDE:
					predictor.setPyrazinamide(susceptibility.getPrediction());
				case Constants.RIFAMPICIN:
					predictor.setRifampicin(susceptibility.getPrediction());
				}

			}
		}

		predictor.setR(result.isR());
		predictor.setMdr(result.isMdr());
		predictor.setXdr(result.isXdr());
		predictor.setTdr(result.isTdr());

		return predictor;
	}

}
