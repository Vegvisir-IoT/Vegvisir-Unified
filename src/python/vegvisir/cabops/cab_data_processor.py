from pathlib import Path
from math import pow

__author__ = "Gloire Rubambiza"
__email__ = "gbr26@cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# @brief: A class for reading and manipulating cab data.
class CabData(object):
    """ A representation of access to raw cab data.

        :param filename: A string.
    """
    def __init__(self, filename):
        self.data_path = Path("cabspottingdata")
        self.filename = self.data_path / (filename)
        self.cab_user_names = []
        self.cab_struct = {}
    
    def cab_triage(self, update_lower_bound, num_users):
        """ Find cabs that fit the limits for the number of updates and
            number of users. Limit users to 200 to comply with MacOS
            file descriptor limits.
            
            :param update_lower_bound: An int.
            :param num_users: An int.
        """
        start = 0
        limit = 200
        cab_dict = {}
        with open(str(self.filename), encoding='utf-8') as fd:
            lines = fd.readlines()

        # Save all the cabs that meet the lower bound
        for line in lines:
            line = line.split('"', 4)
            cab_filename = "new_" + line[1] + ".txt"
            total_updates = int(line[3])

            if total_updates > update_lower_bound:
                if start <= limit and start < num_users:
                    self.cab_struct[line[1]] = [cab_filename]
                    self.cab_user_names.append(line[1])
                    cab_dict[cab_filename] = {'total_updates': total_updates}
                    start += 1
                else:
                    break

        self.__get_file_descriptors()

    def __get_file_descriptors(self):
        """ Initialize a file descriptor for each chosen cab. """

        for _, cab_data in self.cab_struct.items():
            fd = open(str(self.data_path / (cab_data[0])), encoding='utf-8')
            cab_data.append(fd)
    
    def close_file_descriptors(self):
        """ Close all file descriptors when they are no longer needed. """

        for _, cab_data in self.cab_struct.items():
            cab_data[1].close()

    def find_data_maximum(self, output):
        """ Find the maximum latitude and longitude in the data.

            :param output: A string.
        """
        max_long = float('-Inf')
        max_lat = 0
        min_long = 0
        min_lat = float('Inf')

        for _, cab_data in self.cab_struct.items():
            with open(str(self.data_path / (cab_data[0])), encoding='utf-8') as fd:
                line = fd.readline()
                while line:
                    line = line.split()
                    lat = float(line[0])
                    longitude = float(line[1])
                    max_lat = max([lat, max_lat])
                    min_lat = min([lat, min_lat])
                    min_long = min([longitude, min_long])
                    max_long = max([longitude, max_long])
                    line = fd.readline()
                    if not line:
                        print("DONE READING FROM CAB %s!\n" % cab_data[0])
        
        fd = open(output, "w+")
        fd.write("max_lat %s\n" % max_lat)
        fd.write("min_lat %s\n" % min_lat)
        fd.write("max_long %s\n" % max_long)
        fd.write("min_long %s\n" % min_long)
        fd.close()
        data_extremes = {'max_lat': max_lat, 'min_lat': min_lat}
        data_extremes['max_long'] = max_long 
        data_extremes['min_long'] = min_long

        return data_extremes

    def read_data_extremes(self, output):
        """
            Reads the data maxes from a local file.
            @param output: the output file to read from. 
        """
        data_extremes = {}
        with open(output, "r", encoding='utf-8') as fd:
            all_lines = fd.readlines()
            for line in all_lines:
                line = line.split()
                data_extremes[line[0]] = float(line[1])
        return data_extremes
