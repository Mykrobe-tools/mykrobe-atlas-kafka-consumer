package com.makeandship.mykrobe;

public class Constants {

	// processor bindings
	// sources
	public static final String SOURCE_EXPERIMENTS = "source_experiments";
	public static final String SOURCE_PREDICTOR_RESULT = "source_predictor_result";
	public static final String SOURCE_DISTANCE_RESULT = "source_distance_result";
	public static final String SOURCE_PREDICTOR_RESULT_FILE = "source_predictor_result_file";

	// sinks
	public static final String SINK_CORE_EXPERIMENTS = "sink_core_experiments";
	public static final String SINK_CORE_PREDICTOR_RESULT = "sink_core_predictor_result";
	public static final String SINK_CORE_TREE_DISTANCE_RESULT = "sink_core_tree_distance_result";
	public static final String SINK_CORE_NEAREST_NEIGHBOUR_RESULT = "sink_core_nearest_neighbour_result";
	public static final String SINK_CORE_PREDICTOR_RESULT_FILE = "sink_core_predictor_result_file";

	// stores
	public static final String STORE_CORE_EXPERIMENT = "core-experiment-store";
	public static final String STORE_CORE_PREDICTOR_RESULT = "core-predictor-result-store";
	public static final String STORE_CORE_DISTANCE_RESULT = "core-distance-result-store";
	public static final String STORE_CORE_PREDICTOR_RESULT_FILE = "core-predictor-result-file-store";

	// schema registry
	public static final String SCHEMA_REGISTRY_URL = System.getenv("SCHEMA_REGISTRY_URL");

	// dates formats
	public static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd";

	// result types
	public static final String PREDICTOR_RESULT_TYPE = "predictor";
	public static final String DISTANCE_RESULT_TYPE = "distance";

	// result sub-types
	public static final String NEAREST_NEIGHBOUR_SUBTYPE = "nearest-neighbour";
	public static final String TREE_DISTANCE_SUBTYPE = "tree-distance";

	// drugs
	public static final String ISONIAZID = "Isoniazid";
	public static final String KANAMYCIN = "Kanamycin";
	public static final String ETHAMBUTOL = "Ethambutol";
	public static final String STREPTOMYCIN = "Streptomycin";
	public static final String CAPREOMYCIN = "Capreomycin";
	public static final String CIPROFLOXACIN = "Ciprofloxacin";
	public static final String MOXIFLOXACIN = "Moxifloxacin";
	public static final String OFLOXACIN = "Ofloxacin";
	public static final String PYRAZINAMIDE = "Pyrazinamide";
	public static final String AMIKACIN = "Amikacin";
	public static final String RIFAMPICIN = "Rifampicin";

}
