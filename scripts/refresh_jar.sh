# Copyright (C) (2019) (Mathieu Bergeron) (mathieu.bergeron@cmontmorency.qc.ca)
#
# This file is part of aquiletour
#
# aquiletour is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# aquiletour is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with aquiletour.  If not, see <https://www.gnu.org/licenses/>


##### INCLUDE #####
scripts_dir=$(dirname $(readlink -f $0))
. $scripts_dir/include.sh
###################

current_dir=$(pwd)
cd $root_dir

./gradlew jar

cd $current_dir

mv -v $root_dir/build/libs/$jar_name $webapp_dir

zip -r $webapp_dir/$jar_name $webapp_dir/client -x "$webapp_dir/client/ts/**"
