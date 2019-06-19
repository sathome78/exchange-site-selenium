

# Install requirements

    pip install -r requirements.txt
    
# Run parser

    python parser.py -t=prod -o=links_list.txt
    
   both have default values.
   
   The profile is specified with the command ``-t=profile_name``. The default profile is ``prod``.
    
   Available Profiles:
   
    prod = https://exrates.me
    
    demo = https://demo.exrates.me
   
    preprod = http://dev6.exapp
   
    staging = http://dev8.exapp
   