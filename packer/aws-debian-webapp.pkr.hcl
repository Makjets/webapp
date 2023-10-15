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
    delay_seconds = 5
    max_attempts  = 50
  }

  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = [
  "source.amazon-ebs.my-debian-ami", ]

  //   provisioner "file" {
  //     source      = "${var.setup_script}"
  //     destination = "/tmp/${var.setup_script}"
  //   }

  provisioner "shell" {
    script = "${var.setup_scrit}"
  }
}