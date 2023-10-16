packer {
  required_plugins {
    amazon = {
      source  = "github.com/hashicorp/amazon"
      version = ">= 1.0.0"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "aws_profile" {
  type    = string
  default = "dev"
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9" # debian 12 us-east-1
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

variable "subnet_id" {
  type    = string
  default = "subnet-0fd0603dd79ab41f5"
}

variable "setup_script" {
  type    = string
  default = "setup.sh"
}

variable "files" {
  type = object({
    jar_file      = string
    user_csv_file = string
  })
  default = {
    jar_file      = "../build/libs/webapp-0.0.1-SNAPSHOT.jar"
    user_csv_file = "../users.csv"
  }
}

variable "sql_config" {
  type = object({
    sql_user     = string
    sql_password = string
    sql_db       = string
  })
  default = {
    sql_user     = env("MYSQL_USER")
    sql_password = env("MYSQL_PASSWORD")
    sql_db       = env("MYSQL_DB")
  }
}
# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "my-debian-ami" {
  region  = "${var.aws_region}"
  profile = "${var.aws_profile}"
  ami_users = [
    "868203542116",
  ]
  ami_name        = "webapp_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225 webapp"
  ami_regions = [
    "us-east-1",
  ]

  aws_polling {
    delay_seconds = 15
    max_attempts  = 50
  }

  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 25
    volume_type           = "gp2"
  }
}

build {
  sources = [
  "source.amazon-ebs.my-debian-ami", ]

  provisioner "file" {
    source      = "${var.files.jar_file}"
    destination = "/tmp/webapp.jar"
    generated   = true
    timeout     = "2m"
  }

  provisioner "file" {
    source      = "${var.files.user_csv_file}"
    destination = "/tmp/users.csv"
  }

  provisioner "shell" {
    inline = [
      "pwd",
      "sudo mv /tmp/webapp.jar /opt/webapp.jar",
      "sudo mv /tmp/users.csv /opt/users.csv",
    ]
  }

  provisioner "shell" {
    script = "${var.setup_script}"
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1",
      "MYSQL_USER=${var.sql_config.sql_user}",
      "MYSQL_DB=${var.sql_config.sql_db}",
      "MYSQL_PASSWORD=${var.sql_config.sql_password}",
    ]
  }
}