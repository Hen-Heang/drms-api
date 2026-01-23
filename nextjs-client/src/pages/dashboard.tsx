import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/router';
import ProtectedRoute from '@/components/ProtectedRoute';

export default function Dashboard() {
  const { user, logout } = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-gray-100">
        <nav className="bg-white shadow-lg">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between h-16">
              <div className="flex items-center">
                <h1 className="text-2xl font-bold text-gray-800">DRMS Dashboard</h1>
              </div>
              <div className="flex items-center space-x-4">
                <span className="text-gray-700">
                  Welcome, <strong>{user?.fullName}</strong>
                </span>
                <button
                  onClick={handleLogout}
                  className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
                >
                  Logout
                </button>
              </div>
            </div>
          </div>
        </nav>

        <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <div className="px-4 py-6 sm:px-0">
            <div className="bg-white shadow rounded-lg p-6">
              <h2 className="text-2xl font-semibold mb-4">
                {user?.role === 'PARTNER' ? 'Partner Dashboard' : 'Merchant Dashboard'}
              </h2>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                <div className="bg-blue-50 p-4 rounded-lg">
                  <h3 className="font-semibold text-gray-700">Email</h3>
                  <p className="text-gray-600">{user?.email}</p>
                </div>
                <div className="bg-blue-50 p-4 rounded-lg">
                  <h3 className="font-semibold text-gray-700">Role</h3>
                  <p className="text-gray-600">{user?.role}</p>
                </div>
                <div className="bg-blue-50 p-4 rounded-lg">
                  <h3 className="font-semibold text-gray-700">Phone</h3>
                  <p className="text-gray-600">{user?.phone || 'N/A'}</p>
                </div>
                <div className="bg-blue-50 p-4 rounded-lg">
                  <h3 className="font-semibold text-gray-700">Status</h3>
                  <p className="text-gray-600">{user?.status}</p>
                </div>
              </div>

              <div className="border-t pt-4">
                <h3 className="text-lg font-semibold mb-2">Quick Actions</h3>
                <p className="text-gray-600">
                  {user?.role === 'PARTNER'
                    ? 'Partner features are coming soon. You will be able to manage merchants and inventory here.'
                    : 'Merchant features are coming soon. You will be able to view products and place orders here.'}
                </p>
              </div>
            </div>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  );
}
