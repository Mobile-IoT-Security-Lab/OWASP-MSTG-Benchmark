from flask import Flask,request,render_template

app = Flask(__name__)
@app.route('/', methods=['GET','POST'])
def login():
    error = None
    if request.method == 'POST':
        req_Json=request.json
        name=req_Json['username']
        email=req_Json['email']
        if name!= "" or email!="":
            return log_the_user_in(name)
        else:
            error = 'Invalid username/password'
    else:
        return render_template("index.html")
    # the code below is executed if the request method
    # was GET or the credentials were invalid
    return render_template('index.html', error=error)

def valid_login(user,email):
    if user=="" or email =="":
        return False
    return True

def log_the_user_in(name):
    return render_template("profile.html",user=name)
