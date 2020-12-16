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
CREATE INDEX core_experiments_sample_isolate_id ON core_experiments(sampleIsolateId);
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
  `phyloGroup` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `phyloGroupPercentCoverage` float COLLATE utf8mb4_unicode_ci,
  `phyloGroupMedianDepth` bigint(20) COLLATE utf8mb4_unicode_ci,
  `subComplex` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `subComplexPercentCoverage` float COLLATE utf8mb4_unicode_ci,
  `subComplexMedianDepth` bigint(20) COLLATE utf8mb4_unicode_ci,
  `species` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `speciesPercentCoverage` float COLLATE utf8mb4_unicode_ci,
  `speciesMedianDepth` bigint(20) COLLATE utf8mb4_unicode_ci,
  `lineage` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  `lineagePercentCoverage` float COLLATE utf8mb4_unicode_ci,
  `lineageMedianDepth` bigint(20) COLLATE utf8mb4_unicode_ci,
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

DROP TABLE IF EXISTS `core_groups`;
CREATE TABLE `core_groups` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `annotation` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `deleted` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_groups_deleted ON core_groups(deleted);

DROP TABLE IF EXISTS `core_group_experiments`;
CREATE TABLE `core_group_experiments` (
  `groupId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `experimentId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`groupId`, `experimentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_group_experiments_group_id ON core_group_experiments(groupId);

DROP TABLE IF EXISTS `core_group_searches`;
CREATE TABLE `core_group_searches` (
  `groupId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `searchId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`groupId`, `searchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_group_searches_group_id ON core_group_searches(groupId);

DROP TABLE IF EXISTS `core_predictor_result_lineage`;
CREATE TABLE `core_predictor_result_lineage` (
  `experimentId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `index` bigint(20) NOT NULL,
  `lineage` varchar(200) COLLATE utf8mb4_unicode_ci  DEFAULT '',
  PRIMARY KEY (`experimentId`, `index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_predictor_result_lineage_experiment ON core_predictor_result_lineage(experimentId);

DROP TABLE IF EXISTS `core_searches`;
CREATE TABLE `core_searches` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `bigsiSeq` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `bigsiThreshold` DECIMAL(10, 8),
  `bigsiRef` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `bigsiAlt` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `bigsiPos` bigint(20) DEFAULT 0,
  `bigsiGene` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `status` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `expires` datetime,
  `created` datetime,
  `modified` datetime,
  `deleted` bigint(20) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_searches_deleted ON core_searches(deleted);

DROP TABLE IF EXISTS `core_search_results`;
CREATE TABLE `core_search_results` (
  `searchId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resultSampleId` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `received` datetime,
  `reference` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `completedBigsiQueries` bigint(20) DEFAULT 0,
  `totalBigsiQueries` bigint(20) DEFAULT 0,
  `resultGenotype` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '',
  PRIMARY KEY (`searchId`, `resultSampleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX core_search_result_searchId ON core_search_result(searchId);
CREATE INDEX core_search_result_sampleId ON core_search_result(resultSampleId);