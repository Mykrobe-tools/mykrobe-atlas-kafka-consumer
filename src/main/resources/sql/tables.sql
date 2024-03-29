DROP TABLE IF EXISTS `core_experiments`;
CREATE TABLE `core_experiments` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `created` datetime,
  `modified` datetime,
  `outcomeSputumSmearConversion` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `outcomeSputumCultureConversion` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `outcomeWhoOutcomeCategory` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `outcomeDateOfDeath` datetime,
  `patientId` varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientSiteId` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientGenderAtBirth` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientCountryOfBirth` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientAge` bigint(20),
  `patientBmi` DECIMAL(10, 8),
  `patientInjectingDrugUse` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientHomeless` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientImprisoned` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientSmoker` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientDiabetic` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientHivStatus` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `patientArt` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleLabId` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleIsolateId` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleCollectionDate` datetime,
  `sampleProspectiveIsolate` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleCountryIsolate` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleCityIsolate` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleLongitudeIsolate` DECIMAL(11, 8),
  `sampleLatitudeIsolate` DECIMAL(10, 8),
  `sampleDateArrived` datetime,
  `sampleAnatomicalOrigin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `sampleSmear` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `deleted` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_experiments_created ON core_experiments(created);
CREATE INDEX core_experiments_modified ON core_experiments(modified);
CREATE INDEX core_experiments_deleted ON core_experiments(deleted);

DROP TABLE IF EXISTS `core_nearest_neighbour_result`;
CREATE TABLE `core_nearest_neighbour_result` (
  `experimentId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `received` datetime NOT NULL,
  `type` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `subType` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  PRIMARY KEY (`experimentId`, `received`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_nearest_neighbour_result_experiment ON core_nearest_neighbour_result(experimentId);

DROP TABLE IF EXISTS `core_predictor_result`;
CREATE TABLE `core_predictor_result` (
  `experimentId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `received` datetime NOT NULL,
  `kmer` bigint(20) DEFAULT 0,
  `mykrobePredictorVersion` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `mykrobeAtlasVersion` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `genotypeModel` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `externalId` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `isoniazid` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `kanamycin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `ethambutol` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `streptomycin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `capreomycin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `ciprofloxacin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `moxifloxacin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `ofloxacin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `pyrazinamide` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `amikacin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `rifampicin` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `r` tinyint(1) DEFAULT 0,
  `mdr` tinyint(1) DEFAULT 0,
  `xdr` tinyint(1) DEFAULT 0,
  `tdr` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`experimentId`, `received`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_predictor_result_experiment ON core_predictor_result(experimentId);

DROP TABLE IF EXISTS `core_predictor_result_file`;
CREATE TABLE `core_predictor_result_file` (
  `experimentId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `received` datetime NOT NULL,
  `index` bigint(20) NOT NULL,
  `filename` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  PRIMARY KEY (`experimentId`, `received`, `index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_predictor_result_file_experiment ON core_predictor_result_file(experimentId);

DROP TABLE IF EXISTS `core_tree_distance_result`;
CREATE TABLE `core_tree_distance_result` (
  `experimentId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `received` datetime NOT NULL,
  `type` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `subType` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  PRIMARY KEY (`experimentId`, `received`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_tree_distance_result_experiment ON core_tree_distance_result(experimentId);
