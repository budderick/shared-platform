/**
 * Adds a combined geotools bundle and an opengis bundle to the platform.
 */
def geotools(String geotoolsVersion = '10.4',
	List<String> modules = [
		'gt-api',
		'gt-cql',
		'gt-epsg-hsql',
		'gt-geojson',
		'gt-main',
		'gt-shapefile'
	], String bundleVersion = geotoolsVersion + '.0.combined') {
	
	repositories {
		maven {
			url 'http://download.osgeo.org/webdav/geotools/'
		}
		maven {
			url 'http://download.java.net/maven/2'
		}
		maven {
			url 'http://repo.opengeo.org'
		}
	}
	
	platform {
		// opengis
		bundle "org.geotools:gt-opengis:${geotoolsVersion}", {
			bnd {
				symbolicName = 'org.opengis'
			}
		}
		
		// geotools dependencies
		modules.each {
			if (it != 'gt-opengis') {
				bundle "org.geotools:${it}:${geotoolsVersion}"
			}
		}
		
		// geotools bundle
		merge {
			match {
				it.group != null && it.group.startsWith('org.geotools') && it.name != 'gt-opengis'
			}
			
			bnd {
				symbolicName = 'org.geotools'
				bundleName = 'Geotools'
				version = bundleVersion
				instruction 'Export-Package', "org.geotools.*;version=$bundleVersion"
				instruction 'Private-Package', '*'
			}
		}
		
		// geotools depends on log4j
		bundle group: 'log4j', name: 'log4j'
	}
}

// default configuration	
geotools()