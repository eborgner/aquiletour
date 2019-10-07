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
    print "usage %s users_and_port.json apache.server apache.user apache.login Private.template Teacher.template conf_dir" % sys.argv[0]
    exit(0)

INPUT_PATH = sys.argv[1]
APACHE_SERVER = sys.argv[2]
APACHE_USER = sys.argv[3]
APACHE_LOGIN = sys.argv[4]
PRIVATE_TEMPLATE = sys.argv[5]
TEACHER_TEMPLATE = sys.argv[6]
CONF_DIR = sys.argv[7]

SERVER_NAME='aquiletour.ca'
PUBLIC_HTTP_PORT=unicode(80)

PUBLIC_WS_PORT=unicode(80)

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

        line = line.replace("$publicHttpPort", PUBLIC_HTTP_PORT)
        line = line.replace("$publicWsPort", PUBLIC_WS_PORT)
        line = line.replace("$serverName", SERVER_NAME)

        result += line + '\n'

    return result


def generate_file_for_all_users(users_map, TEMPLATE_FILE):

    with codecs.open(TEMPLATE_FILE, encoding='utf8') as template_file:
        _in  = template_file.read()

        for _id in users_map:
            user = users_map[_id]

            out = insert_values(user, _in)

            out_filename = "%s/%s_%s" % (CONF_DIR,_id,TEMPLATE_FILE)

            with codecs.open(out_filename, 'w', encoding='utf8') as out_file:
                out_file.write(out)
                out_file.close()

def generate_apacher_users(users_map, apache_user):
    conf = ""

    for _id in users_map:

        if _id != "login":

            user = users_map[_id]

            out = insert_values(user, apache_user)

            conf += "\n" + out

    return conf

def generate_apache_server(apache_users, apache_server, login_port):
    result = ""

    for line in apache_server.split('\n'):

        line = line.replace("@users", apache_users)
        line = line.replace("$serverName", SERVER_NAME)
        line = line.replace("$httpPort", login_port)

        result += line + "\n"

    return result

def generate_apache_for_all_users(users_map):
    with codecs.open(APACHE_SERVER, encoding='utf8') as apache_server_file:
        with codecs.open(APACHE_USER, encoding='utf8') as apache_user_file:
            with codecs.open(APACHE_LOGIN, encoding='utf8') as apache_login_file:
                apache_server = apache_server_file.read()
                apache_user = apache_user_file.read()
                apache_login = apache_login_file.read()

                apache_users = generate_apacher_users(users_map, apache_user)

                apache_users += "\n" + insert_values(users_map['login'], apache_login)

                login_port = unicode(users_map['login']['privateHttpPort'])

                out = generate_apache_server(apache_users, apache_server, login_port)

                out_filename = "%s/%s.conf" % (CONF_DIR, SERVER_NAME)

                with codecs.open(out_filename, 'w', encoding='utf8') as out_file:
                    out_file.write(out)
                    out_file.close()


with codecs.open(INPUT_PATH, encoding='utf8') as input_file:

    users_map = json.loads(input_file.read())

    generate_file_for_all_users(users_map, PRIVATE_TEMPLATE)
    generate_file_for_all_users(users_map, TEACHER_TEMPLATE)

    generate_apache_for_all_users(users_map)


