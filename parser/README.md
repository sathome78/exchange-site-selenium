

# Install requirements

    pip install -r requirements.txt
    
# Run parser

    python parser.py -t=prod -o=links_list.txt
    
   both have default values.
   
   The profile is specified with the command ``-t=profile_name``. The default profile is ``prod``.
    
   Available Profiles:
   
    prod = https://exrates.me
   
    uat = http://preprod.exapp
   
    dev = http://dev1.exrates.tech
   
    devtest = http://qa1.exrates.tech