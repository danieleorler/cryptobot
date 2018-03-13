import requests
import sys
import time

try:
    url = sys.argv[1]
    time.sleep(30)
    r = requests.get(url)
except requests.exceptions.ConnectionError as e:
    print('Could not connect to "%s", reason: "%s"' % (str(sys.argv), str(e)))
    exit(1)

if r.status_code == 200:
    j = r.json()
    try:
        status = j['status']
        if status == 'UP':
            print("Health check was passed")
            exit(0)
        else:
            print('Health check did not pass. Current application status: "%s"' % str(status))
    except KeyError as e:
        print('Key "%s" not defined in json response: "%s"' % (str(e), str(j)))
        exit(1)
else:
    print('Could not connect to "%s" return status was: "%s"' % (str(sys.argv), str(r.status_code)))
    exit(1)
