#!/bin/bash

scriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
projectDir=${scriptDir}

function createSubModuleFoldersAndFiles() {
    local moduleName=$1
    local subModuleName=$2
    local subModuleDir=${projectDir}/${moduleName}/${moduleName}-${subModuleName}

    local constantSourcePath=src/main/kotlin/com/fondesa/manganow
    local constantTestSourcePath=src/test/kotlin/com/fondesa/manganow

    # Create all the default folders.
    mkdir -p ${subModuleDir}/${constantSourcePath}/${moduleName}/${subModuleName}
    mkdir -p ${subModuleDir}/${constantTestSourcePath}/${moduleName}/${subModuleName}
    # Create an empty build.gradle file.
    touch -a ${subModuleDir}/build.gradle
}

function includeSettingsGradleEntry() {
    local moduleName=$1
    local subModuleName=$2

    local settingsGradleFile=${projectDir}/settings.gradle
    local settingsGradleEntry="include ':${moduleName}:${moduleName}-${subModuleName}'"
    # Check if the entry was previously included in the settings.gradle file.
    if ! grep -q "${settingsGradleEntry}" ${settingsGradleFile}; then
        echo ${settingsGradleEntry} >> ${settingsGradleFile}
    fi
}

function createSubModule() {
    local moduleName=$1
    local subModuleName=$2

    createSubModuleFoldersAndFiles ${moduleName} ${subModuleName}
    includeSettingsGradleEntry ${moduleName} ${subModuleName}
}

moduleName=$1
# The first argument defines the module's name so we should skip it.
shift
for subModuleName
# Creates the sub-module for each argument.
do createSubModule ${moduleName} ${subModuleName}
done