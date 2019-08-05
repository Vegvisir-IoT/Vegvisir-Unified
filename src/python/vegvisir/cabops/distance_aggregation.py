from math import sin, cos, asin, sqrt, radians

__author__ = "Gloire Rubambiza"
__email__ = "gbr26:cornell.edu"
__credits__ = ["Gloire Rubambiza"]


# :brief: A class for finding neighbors near a given cab.
class Neighbors(object):
    """ A representation of the reactangle encompassing all cab locations.

        :param max_lat: A float.
        :param max_lon: A float.
        :param min_lat: A float.
        :param min_long: A float.
    """
    def __init__(self, max_lat, max_lon, min_lat, min_lon):
        self.max_lat = max_lat
        self.max_lon = max_lon
        self.min_lat = min_lat
        self.min_lon = min_lon
    
    def find_by_normalization(self, location, peer_locations, distance):
        """ Find a list of cabs within a certain distance of the
            current cab's location using min-max method.
            :param location: A dictionary, the location of the requesting cab.
            :param peer_locations: A dictionary, the peer's locations.
            :param distance: An int.
        """
        near_by_peers = {}
        my_lat = self.normalize_data(location['latitude'], self.min_lat,
                                    self.max_lat)
        my_lon = self.normalize_data(location['longitude'], self.min_lon,
                                    self.max_lon)

        for peer, peer_info in peer_locations.items():
            
            if location['id'] != peer_info['id']:
                peer_lat = peer_info['latitude']
                peer_lon = peer_info['longitude']
                # Normalize the data
                norm_peer_lat = self.normalize_data(peer_lat, self.min_lat,
                                self.max_lat)
                norm_peer_lon = self.normalize_data(peer_lon, self.min_lon,
                                self.max_lon)

                # Check if the peer falls within the distance
                delta_lat = abs(my_lat - norm_peer_lat)
                delta_lon = abs(my_lon - norm_peer_lon)

                if delta_lat <= distance and delta_lon <= distance:
                    near_by_peers[peer] = [delta_lat, delta_lon]
        # print("Users near %s by norm  are -> %s\n" % (location['id'], near_by_peers))
        return near_by_peers

    def find_by_haversine(self, location, peer_locations, distance):
        """ Find a list of cabs within a certain distance of the
            current cab's location using haversine method. 
            :param location: A dictionary, the location of the requesting cab.
            :param peer_locations: A dictionary, the peer's locations.
            :param distance: An int.
        """
        near_by_peers = {}
        earth_radius = 3959

        min_distance = float('Inf')
        max_distance = 0
        distances = [] 

        for peer, peer_info in peer_locations.items():
            my_id = location['id']

            # Don't check against ourselves
            if my_id != peer_info['id']:
        
                my_lat, my_lon, p_lat, p_lon = map(radians, [location['latitude'],
                                                location['longitude'],
                                                peer_info['latitude'], 
                                                peer_info['longitude']])
                delta_lat = my_lat - p_lat
                delta_lon = my_lon - p_lon

                a = sin(delta_lat/2)**2 + cos(my_lat) * cos(p_lat) * sin(delta_lon/2)**2
                c = 2 * asin(sqrt(a))
                d = float(earth_radius) * c

                d_feet = d * float(5280)
                # print("Distance between peer %s and %s is %d feet\n" % (location['id'],
                # peer, d_feet))

                distances.append(d_feet)
                max_distance = max([d_feet, max_distance])
                min_distance = min([d_feet, min_distance])

                if d_feet <= distance:
                    near_by_peers[peer] = d_feet

        # print("Users near %s by haversine are -> %s\n" % (location['id'], near_by_peers))
        # args = [distances, max_distance, min_distance, distance]
        # self.output_user_status(args)
        return near_by_peers
    
    def normalize_data(self, data, min_x, max_x):
        """
            Normalizes piece of data to fall between 0-1 with 0 being
            the minimum lat/lot and 1 being the maximu lat/long.
            :param data: A float.
            :param min_x: A float, the local minimum.
            :param max_x: A float, the local maximum.
        """
        zi = (data - min_x) / (max_x - min_x)
        return zi

    def output_user_status(self, dimensions):
        """ Output the distance metrics for a given user. 
            :param dimensions: A list.
        """
        distances = dimensions[0]
        max_dist = dimensions[1]
        min_dist = dimensions[2]
        limit = dimensions[3]
        avg_dist = sum(distances)/len(distances)
        
        statement = "Distance limit -> " + str(limit) + " feet \n"
        statement += "Furtherest user away -> " + str(int(max_dist)) + " feet \n"
        statement += "Nearest user away -> " + str(int(min_dist)) + " feet \n"
        statement += "Average distance away -> " + str(int(avg_dist)) + " feet \n"
        print(statement)
