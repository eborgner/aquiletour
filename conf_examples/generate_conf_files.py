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


import sys
import json
import codecs

if len(sys.argv) <= 5:
    print "usage %s users_and_port.json apache.server apache.user apache.login Conf.template conf_dir" % sys.argv[0]
    exit(0)

INPUT_PATH = sys.argv[1]
APACHE_SERVER = sys.argv[2]
APACHE_USER = sys.argv[3]
CONF_TEMPLATE = sys.argv[4]
CONF_DIR = sys.argv[5]

SERVER_NAME='aquiletour.ca'
DEFAULT_HTTP_PORT=unicode(35001)

OUTPUT_APACHE=CONF_DIR + "/aquiletour.conf"

def insert_values(user, template):

    result = ""

    for line in template.split('\n'):

        line = line.replace("$teacherId", user['teacherId'])
        line = line.replace("$teacherToken", user['teacherToken'])
        line = line.replace("$teacherName", user['teacherName'])
        line = line.replace("$teacherSurname", user['teacherSurname'])

        line = line.replace("$privateHttpPort", str(user['privateHttpPort']))
        line = line.replace("$privateWsPort", str(user['privateWsPort']))

        line = line.replace("$publicHttpPort", str(user['publicHttpPort']))
        line = line.replace("$publicWsPort", str(user['publicWsPort']))
        line = line.replace("$serverName", SERVER_NAME)

        result += line + '\n'

    return result


def apache_server_insert_values(apache_server, apache_users):

    result = ""

    for line in apache_server.split('\n'):

        line = line.replace("@users", apache_users)
        line = line.replace("$serverName", SERVER_NAME)
        line = line.replace("$httpPort", DEFAULT_HTTP_PORT)

        result += line + "\n"

    return result



def generate_files(users_map, apache_server, apache_user, conf_template):

    apache_users = ""

    for _id in users_map:
        user = users_map[_id]

        apache_this_user = insert_values(user, apache_user)

        apache_users += "\n" + apache_this_user

        conf_this_user = insert_values(user, conf_template)

        with codecs.open(CONF_DIR + '/%s.json' % _id , 'w', encoding='utf8') as conf_out:
            conf_out.write(conf_this_user)

    apache_server = apache_server_insert_values(apache_server, apache_users)

    with codecs.open(OUTPUT_APACHE, 'w', encoding='utf8') as apache_server_out:
        apache_server_out.write(apache_server)



with codecs.open(INPUT_PATH, encoding='utf8') as input_file:

    users_map = json.loads(input_file.read())

    with codecs.open(APACHE_SERVER, encoding='utf8') as apache_server_file:
        with codecs.open(APACHE_USER, encoding='utf8') as apache_user_file:
            with codecs.open(CONF_TEMPLATE, encoding='utf8') as conf_template_file:

                generate_files(users_map, apache_server_file.read(), apache_user_file.read(), conf_template_file.read())
