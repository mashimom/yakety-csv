runner {
	optimizeRunOrder true
	filterStackTrace true
}
spockReports {
	set 'com.athaydes.spockframework.report.testSourceRoots': 'src/integrationTest/groovy'
	set 'com.athaydes.spockframework.report.showCodeBlocks': true
	set 'com.athaydes.spockframework.report.outputDir': 'build/reports/spock-reports/integrationTest'
}
