DROP VIEW IF EXISTS experiments;
CREATE VIEW experiments AS 
SELECT
  id,
  file,
  created,
  modified,
  patientId AS patient_id,
  patientSiteId AS patient_site_id,
  patientGenderAtBirth AS patient_gender_at_birth,
  patientCountryOfBirth AS patient_country_of_birth,
  patientAge AS patient_age,
  patientBmi AS patient_bmi,
  patientInjectingDrugUse AS patient_injecting_drug_use,
  patientHomeless AS patient_homeless,
  patientImprisoned AS patient_imprisoned,
  patientSmoker AS patient_smoker,
  patientDiabetic AS patient_diabetic,
  patientHivStatus AS patient_hiv_status,
  patientArt AS patient_art,
  sampleLabId AS sample_lab_id,
  sampleIsolateId AS sample_isolate_id,
  sampleCollectionDate AS sample_collection_date,
  sampleDateArrived AS sample_date_arrived,
  sampleProspectiveIsolate AS sample_prospective_isolate,
  sampleCountryIsolate AS sample_country_isolate,
  sampleCityIsolate AS sample_city_isolate,
  sampleLongitudeIsolate AS sample_longitude,
  sampleLatitudeIsolate AS sample_latitude,
  sampleAnatomicalOrigin AS sample_anatomical_origin,
  sampleSmear AS sample_smear,
  outcomeSputumSmearConversion AS outcome_sputum_smear_conversion,
  outcomeSputumCultureConversion AS outcome_sputum_culture_conversion,
  outcomeWhoOutcomeCategory AS outcome_who_category_outcome,
  outcomeDateOfDeath AS outcome_date_of_death 
FROM
  core_experiments 
WHERE
  deleted IS NULL;
DROP VIEW IF EXISTS deleted_experiments;
CREATE VIEW deleted_experiments AS 
SELECT
  id,
  file,
  created,
  modified,
  patientId AS patient_id,
  patientSiteId AS patient_site_id,
  patientGenderAtBirth AS patient_gender_at_birth,
  patientCountryOfBirth AS patient_country_of_birth,
  patientAge AS patient_age,
  patientBmi AS patient_bmi,
  patientInjectingDrugUse AS patient_injecting_drug_use,
  patientHomeless AS patient_homeless,
  patientImprisoned AS patient_imprisoned,
  patientSmoker AS patient_smoker,
  patientDiabetic AS patient_diabetic,
  patientHivStatus AS patient_hiv_status,
  patientArt AS patient_art,
  sampleLabId AS sample_lab_id,
  sampleIsolateId AS sample_isolate_id,
  sampleCollectionDate AS sample_collection_date,
  sampleDateArrived AS sample_date_arrived,
  sampleProspectiveIsolate AS sample_prospective_isolate,
  sampleCountryIsolate AS sample_country_isolate,
  sampleCityIsolate AS sample_city_isolate,
  sampleLongitudeIsolate AS sample_longitude,
  sampleLatitudeIsolate AS sample_latitude,
  sampleAnatomicalOrigin AS sample_anatomical_origin,
  sampleSmear AS sample_smear,
  outcomeSputumSmearConversion AS outcome_sputum_smear_conversion,
  outcomeSputumCultureConversion AS outcome_sputum_culture_conversion,
  outcomeWhoOutcomeCategory AS outcome_who_category_outcome,
  outcomeDateOfDeath AS outcome_date_of_death 
FROM
  core_experiments 
WHERE
  deleted IS NOT NULL;
DROP VIEW IF EXISTS experiment_predictor_results;
CREATE VIEW experiment_predictor_results AS 
SELECT
  e.id,
  r.received,
  r.kmer,
  r.mykrobePredictorVersion AS mykrobe_predictor_version,
  r.mykrobeAtlasVersion AS mykrobe_atlas_version,
  r.genotypeModel AS genotype_model,
  r.externalId AS external_id,
  r.isoniazid,
  r.kanamycin,
  r.ethambutol,
  r.streptomycin,
  r.capreomycin,
  r.ciprofloxacin,
  r.moxifloxacin,
  r.ofloxacin,
  r.pyrazinamide,
  r.amikacin,
  r.rifampicin,
  r.phyloGroup AS phylo_group,
  r.phyloGroupPercentCoverage AS phylo_group_percent_coverage,
  r.phyloGroupMedianDepth AS phylo_group_median_depth,
  r.subComplex AS sub_complex,
  r.subComplexPercentCoverage AS sub_complex_percent_coverage,
  r.subComplexMedianDepth AS sub_complex_median_depth,
  r.species AS species,
  r.speciesPercentCoverage AS species_percent_coverage,
  r.speciesMedianDepth AS species_median_depth,
  r.lineage AS lineage,
  r.lineagePercentCoverage AS lineage_percent_coverage,
  r.lineageMedianDepth AS lineage_median_depth,
  r.r,
  r.mdr,
  r.xdr,
  r.tdr,
  e.file,
  e.created,
  e.modified,
  e.patientId AS patient_id,
  e.patientSiteId AS patient_site_id,
  e.patientGenderAtBirth AS patient_gender_at_birth,
  e.patientCountryOfBirth AS patient_country_of_birth,
  e.patientAge AS patient_age,
  e.patientBmi AS patient_bmi,
  e.patientInjectingDrugUse AS patient_injecting_drug_use,
  e.patientHomeless AS patient_homeless,
  e.patientImprisoned AS patient_imprisoned,
  e.patientSmoker AS patient_smoker,
  e.patientDiabetic AS patient_diabetic,
  e.patientHivStatus AS patient_hiv_status,
  e.patientArt AS patient_art,
  e.sampleLabId AS sample_lab_id,
  e.sampleIsolateId AS sample_isolate_id,
  e.sampleCollectionDate AS sample_collection_date,
  e.sampleDateArrived AS sample_date_arrived,
  e.sampleProspectiveIsolate AS sample_prospective_isolate,
  e.sampleCountryIsolate AS sample_country_isolate,
  e.sampleCityIsolate AS sample_city_isolate,
  e.sampleLongitudeIsolate AS sample_longitude,
  e.sampleLatitudeIsolate AS sample_latitude,
  e.sampleAnatomicalOrigin AS sample_anatomical_origin,
  e.sampleSmear AS sample_smear,
  e.outcomeSputumSmearConversion AS outcome_sputum_smear_conversion,
  e.outcomeSputumCultureConversion AS outcome_sputum_culture_conversion,
  e.outcomeWhoOutcomeCategory AS outcome_who_category_outcome,
  e.outcomeDateOfDeath AS outcome_date_of_death 
FROM
  core_experiments e,
  core_predictor_result r 
WHERE
  r.experimentId = e.id 
  AND deleted IS NULL;