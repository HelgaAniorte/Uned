package es.uned.lsi.eped.pract2016;

import es.uned.lsi.eped.DataStructures.CollectionIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.Set;
import es.uned.lsi.eped.DataStructures.SetIF;

public class AcademiaS implements AcademiaIF {

	private DoctorIF founder;

	public AcademiaS(DoctorIF founder) {
		this.founder = founder;
	}

	@Override
	public DoctorIF getFounder() {
		return founder;
	}

	@Override
	public DoctorIF getDoctor(int id) {
		return findDoctorInStudentsLis(getFounder(), id);
	}

	@Override
	public int size() {
		int counter = founder == null ? 0 : 1 + founder.getStudents().size();

		if (founder != null) {
			CollectionIF<DoctorIF> founderStudents = founder.getStudents();
			IteratorIF<DoctorIF> studentsIterator = founderStudents.iterator();
			while (studentsIterator.hasNext()) {
				counter += getNumberOfStudents(studentsIterator.getNext());
			}
		}
		return counter;
	}

	private int getNumberOfStudents(DoctorIF doctor) {
		int childs = doctor.getStudents().size();
		IteratorIF<DoctorIF> studentsIterator = doctor.getStudents().iterator();
		while (studentsIterator.hasNext()) {
			childs += getNumberOfStudents(studentsIterator.getNext());
		}
		return childs;
	}

	@Override
	public boolean isEmpty() {
		return founder == null;
	}

	@Override
	public boolean contains(DoctorIF e) {
		return getDoctor(e) != null;
	}

	@Override
	public void clear() {
		founder = null;
	}

	@Override
	public IteratorIF<DoctorIF> iterator() {
		SetIF<DoctorIF> all = getAll(founder);
		return all.iterator();
	}

	private SetIF<DoctorIF> getAll(DoctorIF doctor) {
		SetIF<DoctorIF> current = new Set<>(doctor);
		IteratorIF<DoctorIF> studentsIterator = doctor.getStudents().iterator();
		while (studentsIterator.hasNext()) {
			current = current.union(getAll(studentsIterator.getNext()));
		}
		return current;
	}

	@Override
	public void addDoctor(DoctorIF newDoctor, DoctorIF supervisor) {
		DoctorS supervisorDoctor = (DoctorS) getDoctor(supervisor);
		DoctorS student = (DoctorS) getDoctor(newDoctor);
		if (supervisorDoctor != null && student == null) {
			DoctorS newDoctorS = (DoctorS) newDoctor;
			newDoctorS.setSupervisor(supervisorDoctor);
			supervisorDoctor.addStudent(newDoctorS);
		}
	}

	@Override
	public void addSupervision(DoctorIF student, DoctorIF supervisor) {
		//
	}

	private DoctorIF getDoctor(DoctorIF e) {
		DoctorS doctor = (DoctorS) e;
		return findDoctorInStudentsLis(founder, doctor.getId());
	}

	private DoctorIF findDoctorInStudentsLis(DoctorIF doctor, int id) {
		if (doctor == null || doctor.equals(new DoctorS(id))) {
			return doctor;
		} else {
			CollectionIF<DoctorIF> students = doctor.getStudents();
			if (students == null || students.isEmpty()) {
				return null;
			} else {
				IteratorIF<DoctorIF> studentsIt = students.iterator();
				while (studentsIt.hasNext()) {
					DoctorIF d = findDoctorInStudentsLis(studentsIt.getNext(), id);
					if (d != null) {
						return d;
					}
				}
				return null;
			}
		}
	}
}
